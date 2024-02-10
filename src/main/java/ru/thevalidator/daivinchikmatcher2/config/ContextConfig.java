package ru.thevalidator.daivinchikmatcher2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.thevalidator.daivinchikmatcher2.Main;
import ru.thevalidator.daivinchikmatcher2.exception.CanNotContinueException;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.settings.ProfileGeneratorSettings;
import ru.thevalidator.daivinchikmatcher2.util.ImageCheckerUtil;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Bean("matchingWords")
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
        Set<String> maleNames = readNames("data/profile/male/name.txt");
        LOG.debug("Loaded male names: {}", maleNames.size());
        checkCollection(maleNames);
        return maleNames;
    }

    @Bean("femaleNames")
    public Set<String> femaleNames() throws IOException {
        Set<String> femaleNames = readNames("data/profile/female/name.txt");
        LOG.debug("Loaded female names: {}", femaleNames.size());
        checkCollection(femaleNames);
        return femaleNames;
    }

    private Set<String> readNames(String path) throws IOException {
        Set<String> names = new HashSet<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(path),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) {
                    names.add(line.trim());
                }
            }
        }
        return names;
    }

    @Bean("maleText")
    public List<String> maleProfileText() throws IOException {
        List<String> maleTexts = readProfileTextData("data/profile/male/profile_text.txt");
        LOG.debug("Loaded male texts: {}", maleTexts.size());
        checkCollection(maleTexts);
        return maleTexts;
    }

    @Bean("femaleText")
    public List<String> femaleProfileText() throws IOException {
        List<String> femaleTexts = readProfileTextData("data/profile/female/profile_text.txt");
        LOG.debug("Loaded female texts: {}", femaleTexts.size());
        checkCollection(femaleTexts);
        return femaleTexts;
    }

    private List<String> readProfileTextData(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(path), StandardCharsets.UTF_8))) {
            List<String> descriptions = new ArrayList<>();
            String line;
            StringBuilder sb = new StringBuilder();
            boolean isAppending = false;
            while ((line = reader.readLine()) != null && !line.isBlank()) {
                if (line.startsWith("=>")) {
                    sb.setLength(0);
                    isAppending = true;
                } else if (line.startsWith("<=")) {
                    descriptions.add(sb.toString().trim());
                    isAppending = false;
                } else {
                    if (isAppending) {
                        sb.append(line).append("\n");
                    }
                }
            }
            return descriptions;
        }
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
        checkCollection(cities);
        return cities;
    }

    private void checkCollection(Object data) {
        boolean isEmpty = true;
        if (data instanceof Collection) {
            isEmpty = ((Collection<?>) data).isEmpty();
        } else if (data instanceof Map) {
            isEmpty = ((Map<?, ?>) data).isEmpty();
        }
        if (isEmpty) {
            throw new CanNotContinueException("No data found");
        }
    }

    @Bean
    public ProfileGeneratorSettings profileGeneratorSettings() throws IOException {
        Path path = Paths.get("data/config/profile_generator.json");
        String json = Files.readString(path);
        return SerializerUtil.readJson(json, ProfileGeneratorSettings.class);
    }

    @Bean("femalePics")
    public List<String> femalePics() throws IOException {
        List<String> femalePics = readAvatarFolder("data/profile/female/avatar");
        LOG.debug("Loaded female avatars: {}", femalePics.size());
        checkCollection(femalePics);
        return femalePics;
    }

    @Bean("malePics")
    public List<String> malePics() throws IOException {
        List<String> malePics = readAvatarFolder("data/profile/male/avatar");
        LOG.debug("Loaded male avatars: {}", malePics.size());
        checkCollection(malePics);
        return malePics;
    }

    private List<String> readAvatarFolder(String dir) throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(dir))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .filter(ImageCheckerUtil::hasRequiredExtension)
                    .collect(Collectors.toList());
        }
    }

}
