package ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile;

import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.ProfileFillState;

public class ProfileInitResponse {

    private int lastConversationMessageId;
    private ProfileFillState state;
    private DaiVinchikUserProfile profile;

    public DaiVinchikUserProfile getProfile() {
        return profile;
    }

    public void setProfile(DaiVinchikUserProfile profile) {
        this.profile = profile;
    }

    public ProfileFillState getState() {
        return state;
    }

    public void setState(ProfileFillState state) {
        this.state = state;
    }

    public int getLastConversationMessageId() {
        return lastConversationMessageId;
    }

    public void setLastConversationMessageId(int lastConversationMessageId) {
        this.lastConversationMessageId = lastConversationMessageId;
    }

}
