package ru.thevalidator.daivinchikmatcher2.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.responses.GetByIdResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.thevalidator.daivinchikmatcher2.config.settings.Settings;
import ru.thevalidator.daivinchikmatcher2.service.DaiVinchikMessageService;
import ru.thevalidator.daivinchikmatcher2.util.data.SerializerUtil;
import ru.thevalidator.daivinchikmatcher2.vk.dto.DaiVinchikDialogAnswer;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.GetConversationsByIdResponse;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.Response;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.Conversation;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard.Keyboard;

public class DaiVinchikMessageServiceImpl implements DaiVinchikMessageService {

    private static final Logger LOG = LoggerFactory.getLogger(DaiVinchikMessageServiceImpl.class);
    private final VkApiClient vk;
    private final UserActor actor;

    public DaiVinchikMessageServiceImpl(VkApiClient vk, UserActor actor) {
        this.vk = vk;
        this.actor = actor;
    }

    @Override
    public MessageAndKeyboard getDaiVinchikLastMessage() {
        Conversation c = getDaiVinchikConversation();
        Keyboard keyboard = c.getCurrentKeyboard();
        Message message = getMessageById(c.getLastMessageId());
        return new MessageAndKeyboard(message, keyboard);
    }

    @Override
    public void sendMessage(DaiVinchikDialogAnswer answer) {
        try {
            var r = vk.messages().sendUserIds(actor)
                    .userId(Settings.INSTANCE.getDaiVinchickPeerId())
                    .randomId(0)
                    .message(answer.getText())
                    .executeAsString();
                    //.execute();
            //LOG.debug(r.toString());
            System.out.println(r);
        } catch (Exception e) {
        //} catch (ApiException | ClientException e) {
            throw new RuntimeException(e);
        }
        //throw new UnsupportedOperationException("Not supported yet");
    }

    private Conversation getDaiVinchikConversation() {
        try {
            String json = vk.messages()
                    .getConversationsById(actor, Settings.INSTANCE.getDaiVinchickPeerId())
                    .executeAsRaw()
                    .getJson()
                    .getAsString();
            LOG.debug("Receive conversations by id response: {}", json);
            GetConversationsByIdResponse rs = SerializerUtil.getMapper().readValue(json, Response.class).getResponse();
            //@TODO: extract method for checking
            if (rs.getCount() > 1) {
                throw new IllegalArgumentException("Count=" + rs.getCount() + ", but should be 1");
            }
            return rs.getItems().get(0);
        } catch (ClientException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //@TODO: handle all exceptions and errors: https://dev.vk.com/ru/method/messages.getConversationsById
    }

    private Message getMessageById(Integer lastMessageId) {
        try {
            GetByIdResponse rs = vk.messages()
                    .getById(actor)
                    .messageIds(lastMessageId)
                    .execute();
            LOG.debug("Receive messages by id response: {}", rs.toString());
            //@TODO: extract method for checking
            if (rs.getCount() > 1) {
                throw new IllegalArgumentException("Count=" + rs.getCount() + ", but should be 1");
            }
            return rs.getItems().get(0);
        } catch (ApiException | ClientException e) {
            throw new RuntimeException(e);
        }
        //@TODO: handle all exceptions and errors: https://dev.vk.com/ru/method/messages.getById
    }

}
