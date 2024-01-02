package ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Keyboard {
    private Long authorId;

    private List<List<KeyboardButton>> buttons;

    @JsonProperty("inline")
    private Boolean inline;

    /**
     * Should this keyboard disappear on first use
     */
    private Boolean oneTime;

    public Long getAuthorId() {
        return authorId;
    }

    public Keyboard setAuthorId(Long authorId) {
        this.authorId = authorId;
        return this;
    }

    public List<List<KeyboardButton>> getButtons() {
        return buttons;
    }

    public Keyboard setButtons(List<List<KeyboardButton>> buttons) {
        this.buttons = buttons;
        return this;
    }

    public Boolean getInline() {
        return inline;
    }

    public Keyboard setInline(Boolean inline) {
        this.inline = inline;
        return this;
    }

    public Boolean getOneTime() {
        return oneTime;
    }

    public Keyboard setOneTime(Boolean oneTime) {
        this.oneTime = oneTime;
        return this;
    }

    @Override
    public String toString() {
        return "Keyboard{" +
                "authorId=" + authorId +
                ", buttons=" + buttons +
                ", inline=" + inline +
                ", oneTime=" + oneTime +
                '}';
    }

}
