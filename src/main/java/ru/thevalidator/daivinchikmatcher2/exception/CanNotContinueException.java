package ru.thevalidator.daivinchikmatcher2.exception;

import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;

public class CanNotContinueException extends RuntimeException {

    private final MessageAndKeyboard data;

    public CanNotContinueException(MessageAndKeyboard data) {
        super("Something went wrong...");
        this.data = data;
    }

    public CanNotContinueException(String errorMessage) {
        super(errorMessage);
        data = null;
    }

    public MessageAndKeyboard getData() {
        return data;
    }

}
