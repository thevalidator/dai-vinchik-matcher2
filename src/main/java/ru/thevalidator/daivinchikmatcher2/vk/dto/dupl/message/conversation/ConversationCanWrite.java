package ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation;

public class ConversationCanWrite {

    private Boolean allowed;

    private Integer reason;

    public Boolean getAllowed() {
        return allowed;
    }

    public void setAllowed(Boolean allowed) {
        this.allowed = allowed;
    }

    public Integer getReason() {
        return reason;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "ConversationCanWrite{" +
                "allowed=" + allowed +
                ", reason=" + reason +
                '}';
    }

}
