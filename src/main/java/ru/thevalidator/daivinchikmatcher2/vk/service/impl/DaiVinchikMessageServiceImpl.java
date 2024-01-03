package ru.thevalidator.daivinchikmatcher2.vk.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.GetHistoryRev;
import com.vk.api.sdk.objects.messages.responses.GetHistoryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.thevalidator.daivinchikmatcher2.config.settings.Settings;
import ru.thevalidator.daivinchikmatcher2.util.data.SerializerUtil;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.GetConversationsByIdResponse;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.Response;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.Conversation;
import ru.thevalidator.daivinchikmatcher2.vk.service.DaiVinchikMessageService;

public class DaiVinchikMessageServiceImpl implements DaiVinchikMessageService {

    private static final Logger LOG = LoggerFactory.getLogger(DaiVinchikMessageServiceImpl.class);
    private final VkApiClient vk;
    private final UserActor actor;

    public DaiVinchikMessageServiceImpl(VkApiClient vk, UserActor actor) {
        this.vk = vk;
        this.actor = actor;
    }

    @Override
    public Conversation getDaiVinchikConversation() {
        var query = vk.messages().getConversationsById(actor, Settings.INSTANCE.getDaiVinchickPeerId());
        try {
            String json = query
                    .executeAsRaw()
                    .getJson()
                    .getAsString();
            LOG.debug("Receive conversation by id response: {}", json);
            GetConversationsByIdResponse rs = SerializerUtil.getMapper().readValue(json, Response.class).getResponse();
            if (rs.getCount() > 1) {
                throw new IllegalArgumentException("Count=" + rs.getCount() + ", but should be 1");
            }
            return rs.getItems().get(0);
        } catch (ClientException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GetHistoryResponse getDaiVinchikMessageHistoryResponse(Integer fromMessageId, Integer messagesCount, Integer offset) {
        LOG.debug("Send history request get {} last messages", messagesCount);
        try {
            GetHistoryResponse rs = vk.messages().getHistory(actor)
                    .startMessageId(fromMessageId)
                    .offset(offset)
                    .count(messagesCount)
                    //.userId(Settings.INSTANCE.getDaiVinchickPeerId())
                    .peerId(Settings.INSTANCE.getDaiVinchickPeerId())
                    //.extended(true)
                    .rev(GetHistoryRev.REVERSE_CHRONOLOGICAL)
                    .execute();
            LOG.debug("Receive history response: {}", rs.toString());
            return rs;
        } catch (ClientException | ApiException e) {
            throw new RuntimeException(e);
        }
        //@TODO: handle all exceptions and errors: https://dev.vk.com/ru/method/messages.getHistory
    }
}
