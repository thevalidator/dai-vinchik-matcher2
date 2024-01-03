package ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import ru.thevalidator.daivinchikmatcher2.util.data.SerializerUtil;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.Conversation;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetConversationsByIdResponse {
    private Integer count;

    private List<Conversation> items;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Conversation> getItems() {
        return items;
    }

    public void setItems(List<Conversation> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        try {
            return SerializerUtil.getMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
