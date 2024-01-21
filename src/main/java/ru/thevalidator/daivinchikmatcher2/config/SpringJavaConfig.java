package ru.thevalidator.daivinchikmatcher2.config;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.thevalidator.daivinchikmatcher2.account.UserAccount;
import ru.thevalidator.daivinchikmatcher2.vk.custom.actor.CustomUserActor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@ComponentScan("ru.thevalidator.daivinchikmatcher2")
public class SpringJavaConfig {

    @Bean
    public VkApiClient vkApiClient(TransportClient transportClient) {
        return new VkApiClient(transportClient);
    }

    @Bean
    public String userAgent() {
        return "Java VK SDK/1.0";
    }

    @Bean
    public UserActor userActor() throws IOException {
        return new CustomUserActor(userAccount().getToken());
    }

    @Bean
    public UserAccount userAccount() throws IOException {
        Path path = Paths.get("data/config/token");
        String token = Files.readString(path);
        return new UserAccount("user", token);
    }

}
