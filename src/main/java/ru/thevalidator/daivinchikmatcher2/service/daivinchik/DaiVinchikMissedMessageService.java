package ru.thevalidator.daivinchikmatcher2.service.daivinchik;

import com.vk.api.sdk.objects.messages.Message;

public interface DaiVinchikMissedMessageService {

    public String findProfileUrl(Message message);

}
