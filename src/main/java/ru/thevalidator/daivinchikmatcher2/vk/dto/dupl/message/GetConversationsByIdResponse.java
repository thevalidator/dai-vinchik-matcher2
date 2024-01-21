package ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message;

import com.vk.api.sdk.objects.annotations.Required;
import ru.thevalidator.daivinchikmatcher2.util.data.SerializerUtil;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.Conversation;

import java.util.List;

public class GetConversationsByIdResponse {

    @Required
    private Integer count;

    @Required
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
        return SerializerUtil.writeJson(this);
    }

}
