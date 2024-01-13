package ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard;

import com.google.gson.annotations.SerializedName;
import com.vk.api.sdk.objects.annotations.Required;

import java.util.List;

public class Keyboard {

    /**
     * Community or bot, which set this keyboard
     * Entity: owner
     */
    @SerializedName("author_id")
    private Long authorId;

    @Required
    private List<List<KeyboardButton>> buttons;

    private Boolean inline;

    /**
     * Should this keyboard disappear on first use
     */
    @SerializedName("one_time")
    @Required
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
