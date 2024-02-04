package ru.thevalidator.daivinchikmatcher2.service.daivinchik.model;

public class DaiVinchikDialogAnswer {

    private String text;
    private String payload;
    private CaseType type;  //@TODO: probably is not needed

    public DaiVinchikDialogAnswer() {
    }

    public DaiVinchikDialogAnswer(String text, String payload, CaseType type) {
        this.text = text;
        this.payload = payload;
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

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
    @Override
    public String toString() {
        return "DaiVinchikDialogAnswer{" +
                "type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }

    public boolean hasPayload() {
        return payload != null && !payload.isBlank();
    }

}
