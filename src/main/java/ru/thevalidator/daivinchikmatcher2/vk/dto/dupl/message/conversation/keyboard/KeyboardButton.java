package ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard;

public class KeyboardButton {

    private KeyboardButtonAction action;

    private String color;

    public KeyboardButtonAction getAction() {
        return action;
    }

    public void setAction(KeyboardButtonAction action) {
        this.action = action;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "KeyboardButton{" +
                "action=" + action +
                ", color=" + color +
                '}';
    }

}
