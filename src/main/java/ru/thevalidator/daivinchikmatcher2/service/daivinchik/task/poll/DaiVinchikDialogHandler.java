package ru.thevalidator.daivinchikmatcher2.service.daivinchik.task.poll;

import com.vk.api.sdk.objects.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.thevalidator.daivinchikmatcher2.config.settings.Settings;
import ru.thevalidator.daivinchikmatcher2.exception.CanNotContinueException;
import ru.thevalidator.daivinchikmatcher2.exception.TooManyLikesForToday;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikDialogAnswerService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikMessageService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikMissedMessageService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikProfileService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.CaseType;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.DaiVinchikDialogAnswer;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.ProfileFillState;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.DaiVinchikUserProfile;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.ProfileInitResponse;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.statisctic.Statistic;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.task.Task;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.SendMessageResultResponse;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard.KeyboardButtonAction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DaiVinchikDialogHandler implements Task {

    private static final Logger LOG = LoggerFactory.getLogger(DaiVinchikDialogHandler.class);
    public static final int BASE_DELAY = 8;
    private final DaiVinchikMessageService messageService;
    private final DaiVinchikDialogAnswerService answerService;
    private final DaiVinchikMissedMessageService missedMessageService;
    private final DaiVinchikProfileService profileService;
    private final Statistic stats;
    private volatile boolean isActive;
    private int counter = 0;
    private final Random random;

    public DaiVinchikDialogHandler(DaiVinchikMessageService messageService,
                                   DaiVinchikDialogAnswerService answerService,
                                   DaiVinchikMissedMessageService missedMessageService,
                                   DaiVinchikProfileService profileService) {
        this.messageService = messageService;
        this.answerService = answerService;
        this.missedMessageService = missedMessageService;
        this.profileService = profileService;
        this.stats = new Statistic();
        this.random = new Random();
    }

    @Override
    public void run() {
        prepareBeforeStart();
        LOG.debug("Start task");
        MessageAndKeyboard data;
        Integer lastConversationMessageId = messageService.getDaiVinchikLastConversationMessageId();

        if (lastConversationMessageId < 1) {
            lastConversationMessageId = initDaiVinchikConversation();
        }

        int messageDelta;
        while (isActive) {
            try {
                data = messageService.getDaiVinchikLastMessageAndKeyboard();
                messageDelta = data.getMessage().getConversationMessageId() - lastConversationMessageId;
                LOG.info("Iteration: {}, delta {}", ++counter, messageDelta);
                LOG.debug("Message with keyboard: {}", data);
                if (messageDelta > 1) {
                    int from = lastConversationMessageId + 1;
                    int to = data.getMessage().getConversationMessageId();
                    handleMissedMessages(from, to);
                }
                DaiVinchikDialogAnswer answer = answerService.findAnswer(data);
                LOG.debug("Dialog answer found: {}", answer);
                if (answer == null) {
                    isActive = false;
                    break;
                } else if (answer.getType().equals(CaseType.PROFILE_FILLING)) {
                    //ProfileInitResponse rs = profileService.fillRandomProfile(messageService, answerService);
                    DaiVinchikUserProfile profile = profileService.getProfileGenerator().generateRandomProfile();
                    ProfileInitResponse rs = profileService.fillProfile(profile, messageService, answerService);
                    if (rs.getState().equals(ProfileFillState.PARTIAL)) {
                        LOG.debug("Profile fill state: {}, need refill profile", rs.getState());
                        setFillProfileState();
                        rs = profileService.fillProfile(profile, messageService, answerService);
                    }
                    LOG.debug("Profile fill state: {}", rs.getState());
                    lastConversationMessageId = rs.getLastConversationMessageId();
                    TimeUnit.SECONDS.sleep(5);
                    continue;
                } else if (counter == 1 && answer.getType().equals(CaseType.PROFILE_LIKED_ME)) {
                    int to = data.getMessage().getConversationMessageId();
                    int from = to - 2;
                    handleMissedMessages(from, to);
                }
                SendMessageResultResponse resultRs = messageService.sendAnswerMessage(answer);
                lastConversationMessageId = resultRs.getConversationMessageId();
                if (answer.getType().equals(CaseType.PROFILE)) {
                    if (answer.getText().equals("1")) {
                        stats.increaseLikes();
                    } else {
                        stats.increaseDislikes();
                    }
                }
                int sleepingTime = generateSleepingTime();
                System.out.printf("[%s] [%03d] (L_%03d/D_%03d/M_%03d) CASE: %s | ANSWER: %s | SLEEPING %d SECONDS\n",
                        stats.getName(), counter, stats.getLikesSent(),
                        stats.getDislikesSent(), stats.getMatchesCount(),
                        answer.getType(), answer.getText(), sleepingTime);
                TimeUnit.SECONDS.sleep(sleepingTime);
            } catch (TooManyLikesForToday e) {
                if (counter == 1) {
                    SendMessageResultResponse resultRs = messageService
                            .sendAnswerMessage(new DaiVinchikDialogAnswer("1", "1", CaseType.TOO_MANY_LIKES));
                    lastConversationMessageId = resultRs.getConversationMessageId();
                } else {
                    isActive = false;
                    System.out.printf("[%s] [%03d] (L_%03d/D_%03d/M_%03d) TOO MANY LIKES FOR TODAY\n",
                            stats.getName(), counter, stats.getLikesSent(),
                            stats.getDislikesSent(), stats.getMatchesCount());
                }
            } catch (CanNotContinueException e) {
                LOG.error("Error on message data: {}", e.getData());
                isActive = false;
                throw e;
            } catch (Exception e) {
                isActive = false;
                throw new RuntimeException(e);
            }
        }
        LOG.debug("Finish task [{}] (L_{}/D_{}/M_{})",
                counter,
                stats.getLikesSent(),
                stats.getDislikesSent(),
                stats.getMatchesCount());
        stats.setFinishTime(Instant.now());
    }

    private void setFillProfileState() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        MessageAndKeyboard data = messageService.getDaiVinchikLastMessageAndKeyboard();
        DaiVinchikDialogAnswer answer = answerService.findAnswer(data);

        if (!answer.getType().equals(CaseType.INACTIVE_MENU)) {
            while (!answer.getType().equals(CaseType.PROFILE)) {
                messageService.sendAnswerMessage(answer);
                TimeUnit.SECONDS.sleep(5);
                data = messageService.getDaiVinchikLastMessageAndKeyboard();
                answer = answerService.findAnswer(data);
            }

            //go to sleep
            KeyboardButtonAction btn = data.getKeyboard().getButtons().get(0).get(3).getAction();
            messageService.sendAnswerMessage(
                    new DaiVinchikDialogAnswer(btn.getLabel(), btn.getPayload(), CaseType.PROFILE));
            TimeUnit.SECONDS.sleep(1);

            //go to my profile
            data = messageService.getDaiVinchikLastMessageAndKeyboard();
            btn = data.getKeyboard().getButtons().get(0).get(1).getAction();
            messageService.sendAnswerMessage(
                    new DaiVinchikDialogAnswer(btn.getLabel(), btn.getPayload(), CaseType.SLEEPING));
            TimeUnit.SECONDS.sleep(2);
            data = messageService.getDaiVinchikLastMessageAndKeyboard();
        }
        //go to fill profile
        KeyboardButtonAction btn = data.getKeyboard().getButtons().get(0).get(0).getAction();
        messageService.sendAnswerMessage(
                new DaiVinchikDialogAnswer(btn.getLabel(), btn.getPayload(), CaseType.INACTIVE_MENU));
    }

    private Integer initDaiVinchikConversation() {
        var rs = messageService.sendAnswerMessage(new DaiVinchikDialogAnswer("старт", null, CaseType.UNKNOWN));
        return rs.getConversationMessageId();
    }

    private void prepareBeforeStart() {
        Statistic.addStatisticToGlobal(stats);
        stats.setName(Thread.currentThread().getName());
        stats.setStartTime(Instant.now());
        isActive = true;
        random.setSeed(System.currentTimeMillis());
    }

    private int generateSleepingTime() {
        return random.nextInt(10) + 1 + BASE_DELAY;
    }

    private void handleMissedMessages(int from, int to) {
        List<Integer> ids = new ArrayList<>();
        for (int i = from; i < to; i++) {
            ids.add(i);
        }
        var rs = messageService.getDaiVinchikMessagesByConversationId(ids);
        for (Message m: rs) {
            if (m.getFromId().equals(Settings.INSTANCE.getDaiVinchickPeerId())) {
                LOG.warn("MISSED: {}", m);
                String url = missedMessageService.findProfileUrl(m);
                if (url != null) {
                    stats.increaseMatchesCount();
                }
            }
        }
    }

    @Override
    public void stop() {
        //@TODO: implement
        throw new UnsupportedOperationException("Not supported yet");
    }

}

