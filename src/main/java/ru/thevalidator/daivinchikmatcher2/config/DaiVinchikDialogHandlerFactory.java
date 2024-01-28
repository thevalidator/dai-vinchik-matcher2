package ru.thevalidator.daivinchikmatcher2.config;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.thevalidator.daivinchikmatcher2.repository.UserTokenRepository;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikCaseMatcher;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikDialogAnswerService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikMessageService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikMissedMessageService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.impl.DaiVinchikDialogAnswerServiceImpl;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.impl.DaiVinchikMessageServiceImpl;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.task.request.DaiVinchikDialogHandler;
import ru.thevalidator.daivinchikmatcher2.vk.custom.actor.CustomUserActor;
import ru.thevalidator.daivinchikmatcher2.vk.custom.transport.HttpTransportClientWithCustomUserAgent;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DaiVinchikDialogHandlerFactory implements FactoryBean<DaiVinchikDialogHandler> {

    private static final int MAX_HANDLERS = 3;
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final UserTokenRepository tokenRepository;
    private final DaiVinchikMissedMessageService missedMessageService;
    private final DaiVinchikCaseMatcher caseMatcher;
    private final Set<String> matchingWords;


    @Autowired
    public DaiVinchikDialogHandlerFactory(UserTokenRepository tokenRepository,
                                          DaiVinchikMissedMessageService missedMessageService,
                                          DaiVinchikCaseMatcher caseMatcher,
                                          Set<String> matchingWords) {
        this.tokenRepository = tokenRepository;
        this.missedMessageService = missedMessageService;
        this.caseMatcher = caseMatcher;
        this.matchingWords = matchingWords;
    }

    @Override
    public DaiVinchikDialogHandler getObject() {
        if (counter.incrementAndGet() > MAX_HANDLERS) {
            throw new RuntimeException("Too many handlers, maximum allowed: " + MAX_HANDLERS);
        }

        String token = tokenRepository.getTokens().poll();
        DaiVinchikMessageService messageService = getDaiVinchikMessageService(token);
        DaiVinchikDialogAnswerService answerService = new DaiVinchikDialogAnswerServiceImpl(
                caseMatcher, messageService, matchingWords);

        System.out.println(">>> CREATED: " + token);
        return new DaiVinchikDialogHandler(messageService, answerService, missedMessageService);
    }

    private DaiVinchikMessageService getDaiVinchikMessageService(String token) {
        if (token == null) {
            if (tokenRepository.getTokens().isEmpty()) {
                throw new NoSuchElementException("No more tokens found");
            } else {
                throw new IllegalArgumentException("Check tokens, line " + counter.get());
            }
        }
        TransportClient transportClient = new HttpTransportClientWithCustomUserAgent("Java VK SDK/1.0");
        VkApiClient vk = new VkApiClient(transportClient);
        UserActor actor = new CustomUserActor(token);
        return new DaiVinchikMessageServiceImpl(vk, actor);
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
