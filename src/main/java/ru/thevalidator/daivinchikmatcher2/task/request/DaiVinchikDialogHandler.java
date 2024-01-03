package ru.thevalidator.daivinchikmatcher2.task.request;

import com.vk.api.sdk.client.VkApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.thevalidator.daivinchikmatcher2.account.UserAccount;
import ru.thevalidator.daivinchikmatcher2.task.Task;
import ru.thevalidator.daivinchikmatcher2.vk.custom.actor.CustomUserActor;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageWithKeyboard;
import ru.thevalidator.daivinchikmatcher2.vk.service.CaseMatcher;
import ru.thevalidator.daivinchikmatcher2.vk.service.DaiVinchikMessageService;
import ru.thevalidator.daivinchikmatcher2.vk.service.impl.CaseMatcherImpl;
import ru.thevalidator.daivinchikmatcher2.vk.service.impl.DaiVinchikMessageServiceImpl;

public class DaiVinchikDialogHandler implements Task {
    private static final Logger LOG = LoggerFactory.getLogger(DaiVinchikDialogHandler.class);
    private static final Integer LAST_MESSAGES_COUNT = 5;
    private final VkApiClient vk;
    private final UserAccount account;
    private final CustomUserActor actor;
    private final DaiVinchikMessageService service;
    private final CaseMatcher matcher;


    public DaiVinchikDialogHandler(VkApiClient vk, UserAccount account) {
        this.vk = vk;
        this.account = account;
        this.actor = new CustomUserActor(account.getToken());
        service = new DaiVinchikMessageServiceImpl(vk, actor);
        matcher = new CaseMatcherImpl();
    }

    @Override
    public void run() {

        MessageWithKeyboard msg = service.getDaiVinchikLastMessage();

    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet");
    }
}
