package ru.thevalidator.daivinchikmatcher2.service;

import com.vk.api.sdk.objects.messages.Message;

public interface DaiVinchikMissedMessageService {

    public void handleMessage(Message message);

}
