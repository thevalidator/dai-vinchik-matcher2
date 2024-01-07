package ru.thevalidator.daivinchikmatcher2.service;

import com.vk.api.sdk.objects.messages.Message;
import ru.thevalidator.daivinchikmatcher2.vk.dto.DaiVinchikDialogAnswer;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.SendMessageResultResponse;

import java.util.List;

public interface DaiVinchikMessageService {

    public MessageAndKeyboard getDaiVinchikLastMessageAndKeyboard();
    public MessageAndKeyboard getDaiVinchikMessageById(Integer id); //@TODO: probably no need to return message with keyboard

    public Integer getDaiVinchikLastConversationMessageId();
    public List<Message> getDaiVinchikMessagesByConversationId(List<Integer> ids);

    public SendMessageResultResponse sendMessage(DaiVinchikDialogAnswer answer);

}
