package ru.thevalidator.daivinchikmatcher2.vk.task.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.GetHistoryRev;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.responses.GetHistoryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.thevalidator.daivinchikmatcher2.account.UserAccount;
import ru.thevalidator.daivinchikmatcher2.config.settings.Settings;
import ru.thevalidator.daivinchikmatcher2.util.data.SerializerUtil;
import ru.thevalidator.daivinchikmatcher2.vk.custom.actor.CustomUserActor;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.GetConversationsByIdResponse;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.Response;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.Conversation;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard.Keyboard;
import ru.thevalidator.daivinchikmatcher2.vk.task.Task;

import java.util.Objects;

public class DaiVinchikMessageHandler implements Task {
    private static final Logger LOG = LoggerFactory.getLogger(DaiVinchikMessageHandler.class);
    private static final Integer LAST_MESSAGES_NUMBER = 5;
    private final VkApiClient vk;
    private final UserAccount account;
    private final CustomUserActor actor;

    public DaiVinchikMessageHandler(VkApiClient vk, UserAccount account) {
        this.vk = vk;
        this.account = account;
        this.actor = new CustomUserActor(account.getToken());
    }

    @Override
    public void run() {
        GetConversationsByIdResponse crs = getConversationsByIdResponse(Settings.INSTANCE.getDaiVinchickPeerId());
        Conversation c = crs.getItems().get(0);

        Integer lastMessageId = c.getLastMessageId();
        Keyboard keyboard = c.getCurrentKeyboard();

        GetHistoryResponse hrs = getHistoryResponse(LAST_MESSAGES_NUMBER, 0);
        LOG.debug("Receive {} last messages", hrs.getItems().size());
        LOG.debug(hrs.toString());
        for (Message m: hrs.getItems()) {
            if (Objects.equals(m.getFromId(), Settings.INSTANCE.getDaiVinchickPeerId())) {
                System.out.println(">>>>\t" + m.getText());
            }
        }
    }

    private GetHistoryResponse getHistoryResponse(Integer lastMessagesNumber, Integer offset) {
        try {
            return vk.messages().getHistory(actor)
                    .offset(offset)
                    .count(lastMessagesNumber)
                    .userId(Settings.INSTANCE.getDaiVinchickPeerId())
                    //.peerId(519324877L)
                    //.peerId(Settings.INSTANCE.getDaiVinchickPeer())
                    //.extended(true)
                    .rev(GetHistoryRev.REVERSE_CHRONOLOGICAL)
                    .execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private GetConversationsByIdResponse getConversationsByIdResponse(Long... peerIds) {
        var query = vk.messages().getConversationsById(actor, peerIds);
        try {
            String json = query
                    .executeAsRaw()
                    .getJson()
                    .getAsString();

            return SerializerUtil.getMapper().readValue(json, Response.class).getResponse();
        } catch (ClientException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet");
    }
}
