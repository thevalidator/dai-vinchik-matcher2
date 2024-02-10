package ru.thevalidator.daivinchikmatcher2.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.thevalidator.daivinchikmatcher2.repository.UserTokenRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class UserTokenRepositoryImpl implements UserTokenRepository {

    public static final Logger LOG = LoggerFactory.getLogger(UserTokenRepositoryImpl.class);
    private static ConcurrentLinkedQueue<String> tokenQueue;

    public UserTokenRepositoryImpl() {
        tokenQueue = new ConcurrentLinkedQueue<>();
        initTokens();
    }

    private void initTokens() {
        List<String> tokens = readTokens();
        for (String token: tokens) {
            tokenQueue.offer(token);
        }
        LOG.debug("Loaded tokens: {}", tokens.size());
    }

    private List<String> readTokens() {
        List<String> tokens = new ArrayList<>();
        Path path = Paths.get("data/config/token");
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                tokens.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tokens;
    }

    @Override
    public ConcurrentLinkedQueue<String> getTokens() {
        return tokenQueue;
    }

}
