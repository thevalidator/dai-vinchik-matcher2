package ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyboardButtonAction {
    private String label;

    private String payload;

    private String type;

    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "KeyboardButtonAction{" +
                "label='" + label + '\'' +
                ", payload='" + payload + '\'' +
                ", type='" + type + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
