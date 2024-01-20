package ru.thevalidator.daivinchikmatcher2.vk.dto;

import ru.thevalidator.daivinchikmatcher2.service.CaseType;

public class DaiVinchikDialogAnswer {

    private String text;
    private CaseType type;

    public DaiVinchikDialogAnswer() {
    }

    public DaiVinchikDialogAnswer(String text, CaseType type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CaseType getType() {
        return type;
    }

    public void setType(CaseType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DaiVinchikDialogAnswer{" +
                "type='" + type + '\'' +
                ", text=" + text +
                '}';
    }

}
