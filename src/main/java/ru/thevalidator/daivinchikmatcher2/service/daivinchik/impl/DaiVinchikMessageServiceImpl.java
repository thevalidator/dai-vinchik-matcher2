package ru.thevalidator.daivinchikmatcher2.service.daivinchik.impl;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.responses.GetByConversationMessageIdResponse;
import com.vk.api.sdk.objects.messages.responses.GetByIdResponse;
import com.vk.api.sdk.objects.photos.responses.MessageUploadResponse;
import com.vk.api.sdk.objects.photos.responses.SaveMessagesPhotoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.thevalidator.daivinchikmatcher2.config.settings.Settings;
import ru.thevalidator.daivinchikmatcher2.exception.CanNotContinueException;
import ru.thevalidator.daivinchikmatcher2.exception.CanNotLoadImageToVkServerException;
import ru.thevalidator.daivinchikmatcher2.exception.UnexpectedResponseException;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikMessageService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.DaiVinchikDialogAnswer;
import ru.thevalidator.daivinchikmatcher2.util.data.SerializerUtil;
import ru.thevalidator.daivinchikmatcher2.vk.VkTools;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.GetConversationsByIdResponse;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.Response;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.SendMessageResultResponse;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.Conversation;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard.Keyboard;

import javax.imageio.IIOException;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DaiVinchikMessageServiceImpl implements DaiVinchikMessageService {

    private static final Logger LOG = LoggerFactory.getLogger(DaiVinchikMessageServiceImpl.class);
    private final VkApiClient vk;
    private final UserActor actor;

    public DaiVinchikMessageServiceImpl(VkApiClient vk, UserActor actor) {
        this.vk = vk;
        this.actor = actor;
    }

    public DaiVinchikMessageServiceImpl(VkTools vkTools) {
        this.vk = vkTools.getVk();
        this.actor = vkTools.getActor();
    }

    @Override
    public MessageAndKeyboard getDaiVinchikLastMessageAndKeyboard() {
        Conversation c = getDaiVinchikConversation();
        Keyboard keyboard = c.getCurrentKeyboard();
        if (keyboard == null) {
            LOG.warn("Keyboard is null!");
        }
        Message message = getMessageById(c.getLastMessageId());
        if (isNotFromDaiVinchik(message.getFromId())) {
            LOG.debug("Message is not from DaiVinchik: {}", message);
            message = findMessageFromDaiVinchikBefore(message);
        }
        return new MessageAndKeyboard(message, keyboard);
    }

    @Override
    public Message findMessageFromDaiVinchikBefore(Message fromMessage) {
        int previousConversationMsgId = fromMessage.getConversationMessageId() - 1;
        Message message;
        do {
            if (previousConversationMsgId <= 0) { //@TODO: 0 if no dialog with DV => now get error (line 55 add check for id == 0)
                throw new CanNotContinueException("Previous conversation ID equals " + previousConversationMsgId);
            }
            message = getDaiVinchikMessagesByConversationId(List.of(previousConversationMsgId--)).get(0);
        } while (isNotFromDaiVinchik(message.getFromId()));
        return message;
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
        return getMessageByConversationId(ids);
    }

    @Override
    public SendMessageResultResponse sendAnswerMessage(DaiVinchikDialogAnswer answer) {
        try {
            var request = vk.messages()
                    .sendUserIds(actor)
                    .peerId(Settings.INSTANCE.getDaiVinchickPeerId())
                    .randomId(0)
                    .message(answer.getText());
            if (answer.hasPayload()) {
                request.payload(answer.getPayload());
            }
            String rsAsString = request.executeAsString();
            LOG.trace("Receive raw response{}", rsAsString);
            SendMessageResultResponse rs = SerializerUtil.readJson(rsAsString, SendMessageResultResponse.class);
            LOG.trace("Send message result response after serialization: {}", rs);
            //@TODO: handle errors
            // api errors 1116, 1117 ....
            Message m = getMessageById(rs.getResponse());
            rs.setConversationMessageId(m.getConversationMessageId());
            LOG.debug("Send message result response: {}", rs);
            return rs;
        } catch (Exception e) {
            //} catch (ApiException | ClientException e) {
            throw new RuntimeException(e);
        }
        //throw new UnsupportedOperationException("Not supported yet");
    }

    private boolean isNotFromDaiVinchik(Long fromId) {
        return !Objects.equals(fromId, Settings.INSTANCE.getDaiVinchickPeerId());
    }

    private Conversation getDaiVinchikConversation() {
        try {
            String json = vk.messages()
                    .getConversationsById(actor, Settings.INSTANCE.getDaiVinchickPeerId())
                    .executeAsRaw().getContent();
                    //.getJson()
                    //.getAsString();
            LOG.debug("Receive conversations by id response: {}", json);
            GetConversationsByIdResponse rs = SerializerUtil.readJson(json, Response.class).getResponse();
            if (rs == null) {
                throw new UnexpectedResponseException("Response: " + json);
            } else if (rs.getCount() != 1) {  //@TODO: extract method for checking
                throw new IllegalArgumentException("Count=" + rs.getCount() + ", but should be 1");
            }
            return rs.getItems().get(0);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    private Message getMessageById(Integer messageId) {
        try {
            GetByIdResponse rs = vk.messages()
                    .getById(actor)
                    .messageIds(messageId)
                    .execute();
            LOG.debug("Receive messages by id response: {}", rs.toString());
            //@TODO: extract method for checking
            if (rs.getCount() > 1 || rs.getCount() == 0) { //@TODO: if no dialog with DV count == 0 => error, check what is possible to do, if remove check for null then error on return out of bound
                throw new IllegalArgumentException("Count=" + rs.getCount() + ", but should be 1");
            }
            return rs.getItems().get(0);
        } catch (ApiException | ClientException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public String sendPhotoWithDelay(byte[] image, String tmpFilePath) throws Exception {
        SaveMessagesPhotoResponse photoResponse = getSaveMessagesPhotoResponse(image, tmpFilePath);
        long timestamp = System.currentTimeMillis();
        String rs = vk.messages()
                .sendUserIds(actor)
                .peerId(Settings.INSTANCE.getDaiVinchickPeerId())
                .attachment("photo" + photoResponse.getOwnerId() + "_" + photoResponse.getId())
                .randomId((int) timestamp)
                .executeAsString();
        LOG.debug(rs);
        TimeUnit.SECONDS.sleep(2);
        return rs;
    }

    private SaveMessagesPhotoResponse getSaveMessagesPhotoResponse(byte[] image, String tmpFilePath) throws Exception {
        MessageUploadResponse uploadPhotoResponse = null;
        File file = new File(tmpFilePath);
        int attemptCount = 0;

        while ((uploadPhotoResponse == null || uploadPhotoResponse.getPhoto().isBlank()) && ++attemptCount <= 3) {
            var uploadServerUrlResponse = vk.photos().getMessagesUploadServer(actor).execute();
            LOG.debug(uploadServerUrlResponse.toString());

            var uri = uploadServerUrlResponse.getUploadUrl();
            LOG.debug(uri.toString());


            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                outputStream.write(image);
            }
            TimeUnit.SECONDS.sleep(1);

            uploadPhotoResponse = vk.upload().photoMessage(uri.toString(), file).execute();
            LOG.debug(uploadPhotoResponse.toString());
        }

        if (attemptCount > 3) {
            LOG.error(uploadPhotoResponse.toString());
            throw new CanNotLoadImageToVkServerException();
        }
        TimeUnit.SECONDS.sleep(1);

        var savePhotoResponse = vk.photos()
                .saveMessagesPhoto(actor, uploadPhotoResponse.getPhoto())
                .server(uploadPhotoResponse.getServer())
                .hash(uploadPhotoResponse.getHash())
                .execute();
        LOG.debug(savePhotoResponse.toString());

        try {
            Files.delete(file.toPath());
        } catch (IIOException e) {
            LOG.error(e.getMessage());
        }

        return savePhotoResponse.get(0);
    }

}
