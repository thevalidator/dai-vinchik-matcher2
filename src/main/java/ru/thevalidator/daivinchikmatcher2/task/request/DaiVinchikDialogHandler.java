package ru.thevalidator.daivinchikmatcher2.task.request;

import com.vk.api.sdk.client.VkApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.thevalidator.daivinchikmatcher2.account.UserAccount;
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
        LOG.debug("Start task");
        MessageAndKeyboard msg = messageService.getDaiVinchikLastMessage();
        DaiVinchikDialogAnswer answer = answerService.findAnswer(msg);
        messageService.sendMessage(answer);
        LOG.debug("Finish task");
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet");
    }

}

class Statistic {

}
