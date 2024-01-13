package ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation;

import com.google.gson.annotations.SerializedName;
import com.vk.api.sdk.objects.annotations.Required;

public class ConversationSortId {

    @SerializedName("major_id")
    @Required
    private Integer majorId;
    @SerializedName("minor_id")
    @Required
    private Integer minorId;

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public Integer getMinorId() {
        return minorId;
    }

    public void setMinorId(Integer minorId) {
        this.minorId = minorId;
    }

    @Override
    public String toString() {
        return "ConversationSortId{" +
                "majorId=" + majorId +
                ", minorId=" + minorId +
                '}';
    }

}
