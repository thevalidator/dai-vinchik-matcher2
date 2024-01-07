package ru.thevalidator.daivinchikmatcher2.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.responses.GetByConversationMessageIdResponse;
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
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.SendMessageResultResponse;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.Conversation;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class DaiVinchikMessageServiceImpl implements DaiVinchikMessageService {

    private static final Logger LOG = LoggerFactory.getLogger(DaiVinchikMessageServiceImpl.class);
    private final VkApiClient vk;
    private final UserActor actor;

    public DaiVinchikMessageServiceImpl(VkApiClient vk, UserActor actor) {
        this.vk = vk;
        this.actor = actor;
    }

    @Override
    public MessageAndKeyboard getDaiVinchikLastMessageAndKeyboard() {
        Conversation c = getDaiVinchikConversation();
        Keyboard keyboard = c.getCurrentKeyboard();
        Message message = getMessageById(c.getLastMessageId());
        return new MessageAndKeyboard(message, keyboard);
    }

    @Override
    public MessageAndKeyboard getDaiVinchikMessageById(Integer id) {
        Conversation c = getDaiVinchikConversation();
        Keyboard keyboard = c.getCurrentKeyboard();
        Message message = getMessageById(id);
        return new MessageAndKeyboard(message, keyboard);
    }

    @Override
    public Integer getDaiVinchikLastConversationMessageId() {
        Conversation c = getDaiVinchikConversation();
        return c.getLastConversationMessageId();
    }

    @Override
    public List<Message> getDaiVinchikMessagesByConversationId(List<Integer> ids) {
//        List<Message> messages = getMessageByConversationId(ids);
//        List<MessageAndKeyboard> data = new ArrayList<>();
//        for (Message message: messages) {
//            data.add(new MessageAndKeyboard(message, null));
//        }
        return getMessageByConversationId(ids);
    }

    @Override
    public SendMessageResultResponse sendMessage(DaiVinchikDialogAnswer answer) {
        try {
            var r = vk.messages().sendUserIds(actor)
                    .userId(Settings.INSTANCE.getDaiVinchickPeerId())
                    .randomId(0)
                    .message(answer.getText())
                    .executeAsString();
                    //.execute();
            //LOG.debug(r.toString());
            SendMessageResultResponse rs = SerializerUtil.getMapper().readValue(r, SendMessageResultResponse.class);
            //@TODO: handle errors
            Message m = getMessageById(rs.getResponse());
            rs.setConversationMessageId(m.getConversationMessageId());
            System.out.println(rs);
            return rs;
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

    private Message getMessageById(Integer messageId) {
        try {
            GetByIdResponse rs = vk.messages()
                    .getById(actor)
                    .messageIds(messageId)
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

    private List<Message> getMessageByConversationId(List<Integer> ids) {
        try {
            GetByConversationMessageIdResponse rs = vk.messages()
                    .getByConversationMessageId(actor, Settings.INSTANCE.getDaiVinchickPeerId(), ids)
                    .execute();
            LOG.debug("Receive messages by conversation id response: {}", rs.toString());

            return rs.getItems();
        } catch (ApiException | ClientException e) {
            throw new RuntimeException(e);
        }
    }

}
