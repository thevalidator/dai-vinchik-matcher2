package ru.thevalidator.daivinchikmatcher2.vk.custom.actor;

import com.vk.api.sdk.client.actors.UserActor;

public class CustomUserActor extends UserActor {

    private Long userId = null;

    public CustomUserActor(String accessToken) {
        super(null, accessToken);
    }

    public void setId(Long userId) {
        this.userId = userId;
    }

    @Override
    public Long getId() {
        return userId;
    }
}
