package ru.thevalidator.daivinchikmatcher2.task.request;

import com.vk.api.sdk.client.VkApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.thevalidator.daivinchikmatcher2.account.UserAccount;
import ru.thevalidator.daivinchikmatcher2.config.settings.Settings;
import ru.thevalidator.daivinchikmatcher2.service.CaseMatcher;
import ru.thevalidator.daivinchikmatcher2.service.DaiVinchikDialogAnswerService;
import ru.thevalidator.daivinchikmatcher2.service.DaiVinchikMessageService;
import ru.thevalidator.daivinchikmatcher2.service.impl.CaseMatcherImpl;
import ru.thevalidator.daivinchikmatcher2.service.impl.DaiVinchikDialogAnswerServiceImpl;
import ru.thevalidator.daivinchikmatcher2.service.impl.DaiVinchikMessageServiceImpl;
import ru.thevalidator.daivinchikmatcher2.task.Task;
import ru.thevalidator.daivinchikmatcher2.vk.custom.actor.CustomUserActor;
import ru.thevalidator.daivinchikmatcher2.vk.dto.DaiVinchikDialogAnswer;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.SendMessageResultResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DaiVinchikDialogHandler implements Task {

    private static final Logger LOG = LoggerFactory.getLogger(DaiVinchikDialogHandler.class);
    private final CustomUserActor actor;
    private final DaiVinchikMessageService messageService;
    private static final Integer LAST_MESSAGES_COUNT = 5;
    private final VkApiClient vk;
    private final UserAccount account;
    private final CaseMatcher matcher;
    private final DaiVinchikDialogAnswerService answerService;
    private final Statistic stats;
    private boolean isActive;
    private int counter = 0;

    public DaiVinchikDialogHandler(VkApiClient vk, UserAccount account) {
        this.vk = vk;
        this.account = account;
        this.actor = new CustomUserActor(account.getToken());
        messageService = new DaiVinchikMessageServiceImpl(vk, actor);
        answerService = new DaiVinchikDialogAnswerServiceImpl();
        matcher = new CaseMatcherImpl();
        stats = new Statistic();
    }

    @Override
    public void run() {
        isActive = true;
        LOG.debug("Start task");
        MessageAndKeyboard data;
        Integer lastConversationMessageId = messageService.getDaiVinchikLastConversationMessageId();
        LOG.debug("Last conversation message id: {}", lastConversationMessageId);
        int messageDelta;
        while (isActive) {
            data = messageService.getDaiVinchikLastMessageAndKeyboard();
            messageDelta = data.getMessage().getConversationMessageId() - lastConversationMessageId;
            LOG.info("Iteration: {}, delta {}", ++counter, messageDelta);
            LOG.debug("Message with keyboard: {}", data);
            if (messageDelta > 1) {
                handleMissedMessages(lastConversationMessageId + 1, data.getMessage().getConversationMessageId());
            }
            DaiVinchikDialogAnswer answer = answerService.findAnswer(data);
            LOG.debug("Dialog answer found: {}", answer);
            if (answer == null) {
                isActive = false;
                break;
            }
            SendMessageResultResponse resultRs = messageService.sendMessage(answer);
            LOG.debug("Send message result response: {}", resultRs);
            //@TODO: check for response errors
            lastConversationMessageId = resultRs.getConversationMessageId();
            try {
                System.out.println("SLEEPING 12 SECONDS");
                TimeUnit.SECONDS.sleep(12);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        LOG.debug("Finish task");
    }

    private void handleMissedMessages(int from, int to) {
        List<Integer> ids = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            ids.add(i);
        }
        var rs = messageService.getDaiVinchikMessagesByConversationId(ids);
        for (var m: rs) {
            if (m.getFromId().equals(Settings.INSTANCE.getDaiVinchickPeerId())) {
                LOG.info("MISSED: {}",m);
                //@TODO: check for profile
            }
        }
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet");
    }

}

class Statistic {

}
