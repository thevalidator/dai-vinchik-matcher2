package ru.thevalidator.daivinchikmatcher2.vk.dto;

import com.vk.api.sdk.objects.messages.Message;
import ru.thevalidator.daivinchikmatcher2.util.data.SerializerUtil;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard.Keyboard;

public class MessageAndKeyboard {

    private Message message;
    private Keyboard keyboard;

    public MessageAndKeyboard() {
    }

    public MessageAndKeyboard(Message message, Keyboard keyboard) {
        this.message = message;
        this.keyboard = keyboard;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    @Override
    public String toString() {
        try {
            return SerializerUtil.writeJson(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
