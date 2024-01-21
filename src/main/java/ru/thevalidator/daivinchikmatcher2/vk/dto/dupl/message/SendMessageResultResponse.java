package ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message;

public class SendMessageResultResponse {

    private Integer response;
    private Integer conversationMessageId;

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }

    public Integer getConversationMessageId() {
        return conversationMessageId;
    }

    public void setConversationMessageId(Integer conversationMessageId) {
        this.conversationMessageId = conversationMessageId;
    }

    @Override
    public String toString() {
        return "SendMessageResultResponse{" +
                "response=" + response +
                ", conversationMessageId=" + conversationMessageId +
                '}';
    }

}
