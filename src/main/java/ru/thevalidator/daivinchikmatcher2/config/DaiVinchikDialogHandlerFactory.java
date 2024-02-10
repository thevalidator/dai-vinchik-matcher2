package ru.thevalidator.daivinchikmatcher2.config;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.thevalidator.daivinchikmatcher2.repository.UserTokenRepository;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikCaseMatcherService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikDialogAnswerService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikMessageService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikMissedMessageService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.impl.DaiVinchikDialogAnswerServiceImpl;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.impl.DaiVinchikMessageServiceImpl;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.task.poll.DaiVinchikDialogHandler;
import ru.thevalidator.daivinchikmatcher2.vk.VkTools;
import ru.thevalidator.daivinchikmatcher2.vk.custom.transport.HttpTransportClientWithCustomUserAgent;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DaiVinchikDialogHandlerFactory implements FactoryBean<DaiVinchikDialogHandler> {

    private static final int MAX_HANDLERS = 2;
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final UserTokenRepository tokenRepository;
    private final DaiVinchikMissedMessageService missedMessageService;
    private final DaiVinchikCaseMatcherService caseMatcher;
    private final Set<String> matchingWords;


    @Autowired
    public DaiVinchikDialogHandlerFactory(UserTokenRepository tokenRepository,
                                          DaiVinchikMissedMessageService missedMessageService,
                                          DaiVinchikCaseMatcherService caseMatcher,
                                          @Qualifier("matching_words") Set<String> matchingWords) {
        this.tokenRepository = tokenRepository;
        this.missedMessageService = missedMessageService;
        this.caseMatcher = caseMatcher;
        this.matchingWords = matchingWords;
    }

    public boolean hasObject() {
        return !tokenRepository.getTokens().isEmpty() && counter.get() < MAX_HANDLERS;
    }

    @Override
    public DaiVinchikDialogHandler getObject() throws ClientException, ApiException {
        if (counter.incrementAndGet() > MAX_HANDLERS) {
            throw new RuntimeException("Too many handlers, maximum allowed: " + MAX_HANDLERS);
        }

        String token = tokenRepository.getTokens().poll();
        DaiVinchikMessageService messageService = getDaiVinchikMessageService(token);
        DaiVinchikDialogAnswerService answerService = new DaiVinchikDialogAnswerServiceImpl(
                caseMatcher, messageService, matchingWords);

        return new DaiVinchikDialogHandler(messageService, answerService, missedMessageService);
    }

    private DaiVinchikMessageService getDaiVinchikMessageService(String token) throws ClientException, ApiException {
        if (token == null) {
            if (tokenRepository.getTokens().isEmpty()) {
                throw new NoSuchElementException("No more tokens found");
            } else {
                throw new IllegalArgumentException("Check tokens, line " + counter.get());
            }
        }
//        HttpTransportClientWithCustomUserAgent http = new HttpTransportClientWithCustomUserAgent(
//                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
//                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
//                        "Chrome/121.0.0.0 Safari/537.36 Edg/121.0.0.0");
//        VkTools vkTools = new VkTools(http, token);
        VkTools vkTools = new VkTools(new HttpTransportClient(), token);
        return new DaiVinchikMessageServiceImpl(vkTools);
    }

    @Override
    public Class<?> getObjectType() {
        return DaiVinchikDialogHandler.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
        //return !FactoryBean.super.isSingleton();
    }

}
