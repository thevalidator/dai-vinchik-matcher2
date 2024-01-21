package ru.thevalidator.daivinchikmatcher2.task.request;

import com.vk.api.sdk.objects.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.thevalidator.daivinchikmatcher2.config.settings.Settings;
import ru.thevalidator.daivinchikmatcher2.exception.CanNotContinueException;
import ru.thevalidator.daivinchikmatcher2.exception.TooManyLikesForToday;
import ru.thevalidator.daivinchikmatcher2.service.CaseType;
import ru.thevalidator.daivinchikmatcher2.service.DaiVinchikDialogAnswerService;
import ru.thevalidator.daivinchikmatcher2.service.DaiVinchikMessageService;
import ru.thevalidator.daivinchikmatcher2.service.DaiVinchikMissedMessageService;
import ru.thevalidator.daivinchikmatcher2.task.Task;
import ru.thevalidator.daivinchikmatcher2.vk.dto.DaiVinchikDialogAnswer;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.SendMessageResultResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DaiVinchikDialogHandler implements Task {

    private static final Logger LOG = LoggerFactory.getLogger(DaiVinchikDialogHandler.class);
    private final DaiVinchikMessageService messageService;
    private final DaiVinchikDialogAnswerService answerService;
    private final DaiVinchikMissedMessageService missedMessageService;
    private final Statistic stats;
    private volatile boolean isActive;
    private int counter = 0;

    @Autowired
    public DaiVinchikDialogHandler(DaiVinchikMessageService messageService,
                                   DaiVinchikDialogAnswerService answerService,
                                   DaiVinchikMissedMessageService missedMessageService) {
        this.messageService = messageService;
        this.answerService = answerService;
        this.missedMessageService = missedMessageService;
        stats = new Statistic();
    }

    @Override
    public void run() {
        isActive = true;
        LOG.debug("Start task");
        MessageAndKeyboard data;
        Integer lastConversationMessageId = messageService.getDaiVinchikLastConversationMessageId();
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
                }
                SendMessageResultResponse resultRs = messageService.sendMessage(answer);
                //@TODO: check for response errors
                lastConversationMessageId = resultRs.getConversationMessageId();
                if (answer.getType().equals(CaseType.PROFILE) || answer.getType().equals(CaseType.QUESTION_AFTER_PROFILE)) {
                    if (answer.getText().equals("1")) {
                        stats.increaseLikes();
                    } else {
                        stats.increaseDislikes();
                    }
                }
                System.out.printf("[%03d] (L_%03d/D_%03d) CASE: %s | ANSWER: %s | SLEEPING 12 SECONDS\n",
                        counter, stats.getLikes(), stats.getDislikes(), answer.getType(), answer.getText());
                TimeUnit.SECONDS.sleep(12);
            } catch (TooManyLikesForToday e) {
                isActive = false;
            } catch (CanNotContinueException e) {
                LOG.error("Error on message data: {}", e.getData());
                isActive = false;
                throw e;
            } catch (Exception e) {
                isActive = false;
                throw new RuntimeException(e);
            }
        }
        LOG.debug("Finish task");
    }

    private void handleMissedMessages(int from, int to) {
        List<Integer> ids = new ArrayList<>();
        for (int i = from; i < to; i++) { //@TODO: check if the last one not repeats
            ids.add(i);
        }
        var rs = messageService.getDaiVinchikMessagesByConversationId(ids);
        for (Message m: rs) {
            if (m.getFromId().equals(Settings.INSTANCE.getDaiVinchickPeerId())) {
                LOG.info("MISSED: {}", m);
                missedMessageService.findSympathy(m);
            }
        }
    }

    @Override
    public void stop() {
        //@TODO: implement
        throw new UnsupportedOperationException("Not supported yet");
    }

}

class Statistic {

    private int likes;
    private int dislikes;

    public int getLikes() {
        return likes;
    }

    public void increaseLikes() {
        likes++;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void increaseDislikes() {
        dislikes++;
    }

}
