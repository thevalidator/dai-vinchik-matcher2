package ru.thevalidator.daivinchikmatcher2.vk.dto;

public class DaiVinchikDialogAnswer {

    private String text;

    public DaiVinchikDialogAnswer() {
    }

    public DaiVinchikDialogAnswer(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "DaiVinchikDialogAnswer{" +
                "answer='" + text + '\'' +
                '}';
    }

}
