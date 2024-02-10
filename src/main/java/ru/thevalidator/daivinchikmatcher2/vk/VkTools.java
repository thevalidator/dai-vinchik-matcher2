package ru.thevalidator.daivinchikmatcher2.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.thevalidator.daivinchikmatcher2.vk.custom.actor.CustomUserActor;

import java.util.List;

public class VkTools {

    public static final Logger LOG = LoggerFactory.getLogger(VkTools.class);
    private final VkApiClient vk;
    private final UserActor actor;

    public VkTools(VkApiClient vk, UserActor actor) throws ClientException, ApiException {
        this.vk = vk;
        this.actor = actor;
        setActorId();
    }

    public VkTools(String token) throws ClientException, ApiException {
        this.vk = new VkApiClient(new HttpTransportClient());
        this.actor = new CustomUserActor(token);
        setActorId();
    }

    public VkTools(HttpTransportClient httpTransportClient, String token) throws ClientException, ApiException {
        this.vk = new VkApiClient(httpTransportClient);
        this.actor = new CustomUserActor(token);
        setActorId();
    }

    private void setActorId() throws ClientException, ApiException {
        if (actor instanceof CustomUserActor) {
            var c = vk.users().get(actor).fields(List.of(Fields.CONTACTS)).execute();
            Long id = c.get(0).getId();
            ((CustomUserActor) actor).setId(id);
            LOG.debug("USER ID: {}", id);
        }
    }

    public VkApiClient getVk() {
        return vk;
    }

    public UserActor getActor() {
        return actor;
    }

}
