package ru.thevalidator.daivinchikmatcher2.vk.task.longpoll;

import com.vk.api.sdk.client.ClientResponse;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.responses.GetLongPollServerResponse;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.thevalidator.daivinchikmatcher2.account.UserAccount;
import ru.thevalidator.daivinchikmatcher2.config.settings.Settings;
import ru.thevalidator.daivinchikmatcher2.util.data.SerializerUtil;
import ru.thevalidator.daivinchikmatcher2.vk.custom.actor.CustomUserActor;
import ru.thevalidator.daivinchikmatcher2.vk.dto.LongPollServerResponse;
import ru.thevalidator.daivinchikmatcher2.vk.task.Task;

import java.io.IOException;

public class UserLongPollSpectatorService implements Task {
    private static final int WAIT_TIME = Settings.INSTANCE.getLongPollWaitTime();
    private static final int LP_VERSION = Settings.INSTANCE.getLongPollVersion();
    private static final Logger LOG = LoggerFactory.getLogger(UserLongPollSpectatorService.class);
    private final VkApiClient vk;
    private final UserAccount account;
    private volatile boolean isActive;

    public UserLongPollSpectatorService(VkApiClient vk, UserAccount account) {
        this.vk = vk;
        this.account = account;
    }

    @Override
    public void run() {
        isActive = true;
        CustomUserActor actor = new CustomUserActor(account.getToken());
        try {
            LOG.info(">>> START");
            LOG.debug("Send request to long poll server");
            GetLongPollServerResponse rs = getLongPollServerInitDataResponse(actor);
            LOG.debug("Receive response from long poll server");
            String server = rs.getServer();
            String key = rs.getKey();
            Integer ts = rs.getTs();

            ClientResponse response;
            while (isActive) {
                LOG.debug("server: {}   key: {}   ts: {}", server, key, ts);
                String url = getLongPollServerRequestUrl(server, key, ts);
                LOG.debug("Send request for updates");
                response = getLongPollServerResponse(url);
                String responseContent = response.getContent().trim(); //trim because new line added at the end of response
                LOG.info("Receive response: {}", responseContent);

                LongPollServerResponse dto = SerializerUtil.readLongPollResponse(responseContent);
                if (dto.getFailed() != null) {
                    Integer value = dto.getFailed();
                    LOG.error("Got parameter 'failed' with value: {}", value);
                    if (value == 2 || value == 3) {
                        rs = getLongPollServerInitDataResponse(actor);
                        key = rs.getKey();
                        ts = rs.getTs();
                    } else if (value == 1) {
                        ts = dto.getTs();
                    } else if (value == 4) {
                        throw new IllegalArgumentException(String
                                .format("Invalid lp version number: %d, should be from %d to %d",
                                        LP_VERSION, dto.getMinVersion(), dto.getMaxVersion()));
                    } else {
                        throw new UnsupportedMessageTypeException(String
                                .format("Unknown parameter value: failed=%d", value));
                    }
                } else {
                    ts = dto.getTs();
                }
                LOG.debug("ACTIVE: " + isActive);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            isActive = false;
            LOG.info("<<< STOP");
        }
    }

    private ClientResponse getLongPollServerResponse(String url) throws IOException {
        return vk.getTransportClient().get(url);
    }

    private String getLongPollServerRequestUrl(String server, String key, Integer ts) {
        return String
                .format("https://%s?act=a_check&key=%s&ts=%d&wait=%d&mode=2&version=%d",
                        server, key, ts, WAIT_TIME, LP_VERSION);
    }

    private GetLongPollServerResponse getLongPollServerInitDataResponse(CustomUserActor actor)
            throws ApiException, ClientException {
        return vk.messages().getLongPollServer(actor).lpVersion(LP_VERSION).execute();
    }

    @Override
    public void stop() {
        isActive = false;
        LOG.debug("DEACTIVATE METHOD INVOKED");
    }
}
