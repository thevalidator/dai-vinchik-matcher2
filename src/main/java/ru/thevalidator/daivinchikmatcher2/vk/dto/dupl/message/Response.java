package ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    private GetConversationsByIdResponse response;

    public GetConversationsByIdResponse getResponse() {
        return response;
    }

    public void setResponse(GetConversationsByIdResponse response) {
        this.response = response;
    }
}
