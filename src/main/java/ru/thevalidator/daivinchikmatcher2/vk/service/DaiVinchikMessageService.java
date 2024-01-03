package ru.thevalidator.daivinchikmatcher2.vk.service;

import com.vk.api.sdk.objects.messages.responses.GetHistoryResponse;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.Conversation;

public interface DaiVinchikMessageService {

    public Conversation getDaiVinchikConversation();
    public GetHistoryResponse getDaiVinchikMessageHistoryResponse(Integer fromMessageId, Integer messagesCount, Integer offset);

}
