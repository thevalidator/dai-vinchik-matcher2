package ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation;

import com.google.gson.annotations.SerializedName;
import com.vk.api.sdk.objects.annotations.Required;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard.Keyboard;

import java.util.List;

public class Conversation {

    @SerializedName("can_write")
    private ConversationCanWrite canWrite;

    @SerializedName("current_keyboard")
    private Keyboard currentKeyboard;

    @SerializedName("important")
    private Boolean important;

    /**
     * Last message user have read
     */
    @SerializedName("in_read")
    @Required
    private Integer inRead;

    /**
     * Is this conversation uread
     */
    @SerializedName("is_marked_unread")
    private Boolean isMarkedUnread;

    /**
     * Conversation message ID of the last message in conversation
     */
    @SerializedName("last_conversation_message_id")
    private Integer lastConversationMessageId;

    /**
     * ID of the last message in conversation
     */
    @SerializedName("last_message_id")
    @Required
    private Integer lastMessageId;

    /**
     * Ids of messages with mentions
     */
    @SerializedName("mentions")
    private List<Integer> mentions;

    /**
     * Last outcoming message have been read by the opponent
     */
    @SerializedName("out_read")
    @Required
    private Integer outRead;

    @SerializedName("sort_id")
    private ConversationSortId sortId;

    //@SerializedName("chat_settings")
    //private ChatSettings chatSettings;

    //private MessageRequestData messageRequestData;

    //private OutReadBy outReadBy;

    //private ConversationPeer peer;

    //private PushSettings pushSettings;

    //private ConversationSpecialServiceType specialServiceType;

    private Boolean unanswered;

    @SerializedName("unread_count")
    private Integer unreadCount;

    public ConversationCanWrite getCanWrite() {
        return canWrite;
    }

    public void setCanWrite(ConversationCanWrite canWrite) {
        this.canWrite = canWrite;
    }

    public Keyboard getCurrentKeyboard() {
        return currentKeyboard;
    }

    public void setCurrentKeyboard(Keyboard currentKeyboard) {
        this.currentKeyboard = currentKeyboard;
    }

    public Boolean getImportant() {
        return important;
    }

    public void setImportant(Boolean important) {
        this.important = important;
    }

    public Integer getInRead() {
        return inRead;
    }

    public void setInRead(Integer inRead) {
        this.inRead = inRead;
    }

    public Boolean getMarkedUnread() {
        return isMarkedUnread;
    }

    public void setMarkedUnread(Boolean markedUnread) {
        isMarkedUnread = markedUnread;
    }

    public Integer getLastConversationMessageId() {
        return lastConversationMessageId;
    }

    public void setLastConversationMessageId(Integer lastConversationMessageId) {
        this.lastConversationMessageId = lastConversationMessageId;
    }

    public Integer getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(Integer lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public List<Integer> getMentions() {
        return mentions;
    }

    public void setMentions(List<Integer> mentions) {
        this.mentions = mentions;
    }

    public Integer getOutRead() {
        return outRead;
    }

    public void setOutRead(Integer outRead) {
        this.outRead = outRead;
    }

    public ConversationSortId getSortId() {
        return sortId;
    }

    public void setSortId(ConversationSortId sortId) {
        this.sortId = sortId;
    }

    public Boolean getUnanswered() {
        return unanswered;
    }

    public void setUnanswered(Boolean unanswered) {
        this.unanswered = unanswered;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "canWrite=" + canWrite +
                ", currentKeyboard=" + currentKeyboard +
                ", important=" + important +
                ", inRead=" + inRead +
                ", isMarkedUnread=" + isMarkedUnread +
                ", lastConversationMessageId=" + lastConversationMessageId +
                ", lastMessageId=" + lastMessageId +
                ", mentions=" + mentions +
                ", outRead=" + outRead +
                ", sortId=" + sortId +
                ", unanswered=" + unanswered +
                ", unreadCount=" + unreadCount +
                '}';
    }

}
