package ru.thevalidator.daivinchikmatcher2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.thevalidator.daivinchikmatcher2.Main;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.settings.ProfileGeneratorSettings;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.settings.gender.GenderToSearchSettings;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.settings.gender.UserGenderSettings;
import ru.thevalidator.daivinchikmatcher2.util.data.SerializerUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Configuration
@ComponentScan("ru.thevalidator.daivinchikmatcher2")
public class ContextConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ContextConfig.class);

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
        LOG.info(version);
        return version;
    }

    @Bean("matching_words")
    public Set<String> matchingWords() throws IOException {
        Set<String> words = new HashSet<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("data/words.txt"),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) {
                    words.add(line.trim().toLowerCase());
                }
            }
        }
        LOG.debug("Loaded matching words: {}", words.size());
        return words;
    }

    @Bean("maleNames")
    public Set<String> maleNames() throws IOException {
        Set<String> maleNames = new HashSet<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("data/profile/male.txt"),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) {
                    maleNames.add(line.trim());
                }
            }
        }
        LOG.debug("Loaded male names: {}", maleNames.size());
        return maleNames;
    }

    @Bean("femaleNames")
    public Set<String> femaleNames() throws IOException {
        Set<String> femaleNames = new HashSet<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("data/profile/female.txt"),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) {
                    femaleNames.add(line.trim());
                }
            }
        }
        LOG.debug("Loaded female names: {}", femaleNames.size());
        return femaleNames;
    }

    @Bean("cities")
    public Map<String, Integer> cities() throws IOException {
        Map<String, Integer> cities = new HashMap<>();
        try (LineNumberReader reader = new LineNumberReader(
                new InputStreamReader(new FileInputStream("data/profile/cities.txt"),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.trim().split("=");
                if (data.length != 2) {
                    LOG.error("Error in line {}", reader.getLineNumber());
                    continue;
                }
                String city = data[0].trim();
                Integer weight = Integer.parseInt(data[1].trim());
                cities.put(city, weight);
            }
        }
        LOG.debug("Loaded cities: {}", cities.size());
        return cities;
    }

    @Bean
    public ProfileGeneratorSettings profileGeneratorSettings() throws IOException {
        Path path = Paths.get("data/config/profile_generator.json");
        String json = Files.readString(path);

        return SerializerUtil.readJson(json, ProfileGeneratorSettings.class);

//        UserGenderSettings userGender = new UserGenderSettings(50, 50);
//        GenderToSearchSettings genderToSearch = new GenderToSearchSettings(0, 33, 0);
//        return new ProfileGeneratorSettings(18, 28, userGender, genderToSearch);
    }

}
