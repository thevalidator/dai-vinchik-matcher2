package ru.thevalidator.daivinchikmatcher2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.thevalidator.daivinchikmatcher2.Main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Configuration
@ComponentScan("ru.thevalidator.daivinchikmatcher2")
public class ContextConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ContextConfig.class);

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
        LOG.debug("Loaded {} words for matching", words.size());
        return words;
    }

    @Bean
    private static String appVersion() {
        String path = "/version.prop";
        String version = "UNKNOWN";
        try (InputStream stream = Main.class.getResourceAsStream(path)) {
            if (stream != null) {
                Properties props = new Properties();
                try {
                    props.load(stream);
                    stream.close();
                    version = "APP VERSION: " + props.get("version");
                } catch (IOException ignored) {
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOG.debug(version);
        return version;
    }

}
