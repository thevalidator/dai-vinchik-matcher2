package ru.thevalidator.daivinchikmatcher2.config;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.thevalidator.daivinchikmatcher2.account.UserAccount;
import ru.thevalidator.daivinchikmatcher2.vk.custom.actor.CustomUserActor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Configuration
@ComponentScan("ru.thevalidator.daivinchikmatcher2")
public class SpringJavaConfig {

    private static final Logger LOG = LoggerFactory.getLogger(SpringJavaConfig.class);

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

    @Bean
    public Set<String> matchingWords() {
        Set<String> words = new HashSet<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("data/words.txt"),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
        }
        LOG.info("Loaded {} words for matching", words.size());
        return words;
    }

}
