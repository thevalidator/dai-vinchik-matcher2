package ru.thevalidator.daivinchikmatcher2.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.Fields;
import ru.thevalidator.daivinchikmatcher2.vk.custom.actor.CustomUserActor;

import java.util.List;

public class VkTools {

    private final VkApiClient vk;
    private final UserActor actor;

    public VkTools(VkApiClient vk, UserActor actor) {
        this.vk = vk;
        this.actor = actor;
        setActorId();
    }
    public VkTools(String token) {
        this.vk = new VkApiClient(new HttpTransportClient());
        this.actor = new CustomUserActor(token);
        setActorId();
    }

    public VkTools(HttpTransportClient httpTransportClient, String token) {
        this.vk = new VkApiClient(httpTransportClient);
        this.actor = new CustomUserActor(token);
        setActorId();
    }

    private void setActorId() {
        if (actor instanceof CustomUserActor) {
            try {
                var c = vk.users().get(actor).fields(List.of(Fields.CONTACTS)).execute();
                System.out.println(c);
                Long id = c.get(0).getId();
                ((CustomUserActor) actor).setId(id);
                System.out.println(">> USER ID: " + id);
            } catch (ApiException | ClientException ignored) {
                //throw new RuntimeException(ignored);
            }
        }
    }


    public VkApiClient getVk() {
        return vk;
    }

    public UserActor getActor() {
        return actor;
    }

}
