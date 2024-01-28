package ru.thevalidator.daivinchikmatcher2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Configuration
@ComponentScan("ru.thevalidator.daivinchikmatcher2")
public class SpringJavaConfig {

    private static final Logger LOG = LoggerFactory.getLogger(SpringJavaConfig.class);

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
