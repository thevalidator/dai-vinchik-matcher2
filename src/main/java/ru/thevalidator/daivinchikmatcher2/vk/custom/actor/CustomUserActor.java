package ru.thevalidator.daivinchikmatcher2.vk.custom.actor;

import com.vk.api.sdk.client.actors.UserActor;

public class CustomUserActor extends UserActor {
    public CustomUserActor(String accessToken) {
        super(null, accessToken);
    }
}
