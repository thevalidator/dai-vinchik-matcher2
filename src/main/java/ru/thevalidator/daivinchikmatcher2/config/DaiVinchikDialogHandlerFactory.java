package ru.thevalidator.daivinchikmatcher2.config;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.thevalidator.daivinchikmatcher2.repository.UserTokenRepository;
import ru.thevalidator.daivinchikmatcher2.service.CaseMatcher;
import ru.thevalidator.daivinchikmatcher2.service.DaiVinchikDialogAnswerService;
import ru.thevalidator.daivinchikmatcher2.service.DaiVinchikMessageService;
import ru.thevalidator.daivinchikmatcher2.service.DaiVinchikMissedMessageService;
import ru.thevalidator.daivinchikmatcher2.service.impl.DaiVinchikDialogAnswerServiceImpl;
import ru.thevalidator.daivinchikmatcher2.service.impl.DaiVinchikMessageServiceImpl;
import ru.thevalidator.daivinchikmatcher2.task.request.DaiVinchikDialogHandler;
import ru.thevalidator.daivinchikmatcher2.vk.custom.actor.CustomUserActor;
import ru.thevalidator.daivinchikmatcher2.vk.custom.transport.HttpTransportClientWithCustomUserAgent;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DaiVinchikDialogHandlerFactory implements FactoryBean<DaiVinchikDialogHandler> {

    private final UserTokenRepository tokenRepository;
    private final DaiVinchikMissedMessageService missedMessageService;
    private final CaseMatcher caseMatcher;
    private final Set<String> matchingWords;
    private static final AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    public DaiVinchikDialogHandlerFactory(UserTokenRepository tokenRepository,
                                          DaiVinchikMissedMessageService missedMessageService,
                                          CaseMatcher caseMatcher,
                                          Set<String> matchingWords) {
        this.tokenRepository = tokenRepository;
        this.missedMessageService = missedMessageService;
        this.caseMatcher = caseMatcher;
        this.matchingWords = matchingWords;
    }

    @Override
    public DaiVinchikDialogHandler getObject() {

        String token = tokenRepository.getTokens().poll();
        if (counter.incrementAndGet() > 2 || token == null) { //@TODO: 'token null' separate check, add 'queue is empty' check
            throw new RuntimeException("Too many handlers");
        }

        TransportClient transportClient = new HttpTransportClientWithCustomUserAgent("Java VK SDK/1.0");
        VkApiClient vk = new VkApiClient(transportClient);
        UserActor actor = new CustomUserActor(token);
        DaiVinchikMessageService messageService = new DaiVinchikMessageServiceImpl(vk, actor);

        DaiVinchikDialogAnswerService answerService = new DaiVinchikDialogAnswerServiceImpl(
                caseMatcher, messageService, matchingWords);

        System.out.println(">>> CREATED: " + token);
        return new DaiVinchikDialogHandler(messageService, answerService, missedMessageService);
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
