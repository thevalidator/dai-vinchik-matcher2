package ru.thevalidator.daivinchikmatcher2.service.daivinchik.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikDialogAnswerService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikMessageService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikProfileGenerator;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikProfileService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.CaseType;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.DaiVinchikDialogAnswer;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.ProfileFillState;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.DaiVinchikUserProfile;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.Gender;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.ProfileInitResponse;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.SendMessageResultResponse;

import java.util.concurrent.TimeUnit;

@Component
public class DaiVinchikProfileServiceImpl implements DaiVinchikProfileService {

    private static final int TOTAL_PROFILE_QUESTIONS_COUNT = 8;
    private final DaiVinchikProfileGenerator profileGenerator;

    @Autowired
    public DaiVinchikProfileServiceImpl(DaiVinchikProfileGenerator profileGenerator) {
        this.profileGenerator = profileGenerator;
    }

    @Override
    public ProfileInitResponse fillRandomProfile(DaiVinchikMessageService messageService, DaiVinchikDialogAnswerService answerService) {
        DaiVinchikUserProfile profile = profileGenerator.generateRandomProfile();
        return fillProfile(profile, messageService, answerService);

        //Я знакомлю красивых людей и нахожу друзей по интересам! 😍<br><br>Создай анкету и посмотри кому ты понравишься 🔥<br><br>Жми Начать 👇👇👇
        // >> [4, 2, 19, -91050183, 1706271699, Начать, {payload={"command":"start"}, title= ... }, {}]

        //Я помогу найти тебе пару или просто друзей. Можно я задам тебе пару вопросов? keyboard={one_time=true, buttons=[[{action={type=text, payload=, label=👍}, color=positive}]]}}

        //??? Сколько тебе лет?, {title= ... , keyboard={one_time=false, buttons=[[{action={type=text, payload=, label=24}, color=default}]]}}, {}]
        //

        //??? Теперь определимся с полом, {title= ... , keyboard={one_time=false, buttons=[[{action={type=text, payload=1, label=Я девушка}, color=default}, {action={type=text, payload=2, label=Я парень}, color=default}]]}}, {}]
        //

        //Кто тебе интересен?
        //

        //Из какого ты города?
        //

        //Как мне тебя называть?
        //

        //Расскажи о себе и кого хочешь найти, чем предлагаешь заняться. Это поможет лучше подобрать тебе компанию.
        //

        //Теперь пришли свое фото, его будут видеть другие пользователи
        //

        // Все верно?,
        //>> Да, {payload=1

        //"Укажи правильный возраст, только цифры"

    }

    @Override
    public ProfileInitResponse fillProfile(DaiVinchikUserProfile profile,
                                           DaiVinchikMessageService messageService,
                                           DaiVinchikDialogAnswerService answerService) {
        boolean isFinished = false;
        System.out.println(">> PROFILING STARTED");
        int controlQuestionCount = 0;
        int lastConversationMessageId;
        SendMessageResultResponse rs = null;
        try {
            while (!isFinished) {
                TimeUnit.SECONDS.sleep(5);

                MessageAndKeyboard data = messageService.getDaiVinchikLastMessageAndKeyboard();
                String msgText = data.getMessage().getText();

                if (msgText.contains("знакомлю красивых людей и нахожу друзей")
                        || msgText.contains("Жми Начать")) {
                    rs = messageService.sendAnswerMessage(
                            new DaiVinchikDialogAnswer(
                                    data.getKeyboard().getButtons().get(0).get(0).getAction().getLabel(),
                                    data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload(),
                                    CaseType.UNKNOWN));
                } else if (msgText.contains("задам тебе пару вопросов")) {
                    rs = messageService.sendAnswerMessage(
                            new DaiVinchikDialogAnswer(
                                    data.getKeyboard().getButtons().get(0).get(0).getAction().getLabel(),
                                    data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload(),
                                    CaseType.UNKNOWN));
                } else if (msgText.contains("Сколько тебе лет?")) {
                    rs = messageService.sendAnswerMessage(
                            new DaiVinchikDialogAnswer(
                                    String.valueOf(profile.getAge()),
                                    null,
                                    CaseType.UNKNOWN));
                    controlQuestionCount++;
                } else if (msgText.contains("определимся с полом")) {
                    var action = profile.getGender().equals(Gender.FEMALE) ?
                            data.getKeyboard().getButtons().get(0).get(0).getAction()
                            : data.getKeyboard().getButtons().get(0).get(1).getAction();
                    rs = messageService.sendAnswerMessage(new DaiVinchikDialogAnswer(
                            action.getLabel(),
                            action.getPayload(),
                            CaseType.UNKNOWN));
                    controlQuestionCount++;
                } else if (msgText.contains("Кто тебе интересен?")) {
                    var action = profile.getGenderToSearch().equals(Gender.FEMALE) ?    //@TODO: check correct gender selection
                            data.getKeyboard().getButtons().get(0).get(0).getAction()
                            : profile.getGender().equals(Gender.MALE) ?
                            data.getKeyboard().getButtons().get(0).get(1).getAction() :
                            data.getKeyboard().getButtons().get(0).get(2).getAction();

                    rs = messageService.sendAnswerMessage(new DaiVinchikDialogAnswer(
                            action.getLabel(),
                            action.getPayload(),
                            CaseType.UNKNOWN));
                    controlQuestionCount++;
                } else if (msgText.contains("Из какого ты города?")) {
                    messageService.sendAnswerMessage(
                            new DaiVinchikDialogAnswer(
                                    profile.getCity(),
                                    null,
                                    CaseType.UNKNOWN));
                    controlQuestionCount++;
                } else if (msgText.contains("Как мне тебя называть?")) {
                    rs = messageService.sendAnswerMessage(
                            new DaiVinchikDialogAnswer(
                                    profile.getName(),
                                    null,
                                    CaseType.UNKNOWN));
                    controlQuestionCount++;
                } else if (msgText.contains("Расскажи о себе и кого хочешь найти")) {
                    rs = messageService.sendAnswerMessage(
                            new DaiVinchikDialogAnswer(
                                    profile.getProfileText(),
                                    null,
                                    CaseType.UNKNOWN));
                    controlQuestionCount++;
                } else if (msgText.contains("Теперь пришли свое фото")) {
                    String sendPhotoRs = messageService.sendPhotoWithDelay(profile.getPhoto());
                    //@TODO: do I need this response?
                    controlQuestionCount++;
                } else if (msgText.contains("Все верно?")) {
                    rs = messageService.sendAnswerMessage(
                            new DaiVinchikDialogAnswer(
                                    data.getKeyboard().getButtons().get(0).get(0).getAction().getLabel(),
                                    data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload(),
                                    CaseType.UNKNOWN));
                    isFinished = true;
                    controlQuestionCount++;

                } else {
                    DaiVinchikDialogAnswer answer = answerService.findAnswer(data);
                    rs = messageService.sendAnswerMessage(answer);
                    if (answer.getType().equals(CaseType.PROFILE)) {
                        isFinished = true;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        lastConversationMessageId = rs.getConversationMessageId();
        ProfileInitResponse response = new ProfileInitResponse();
        response.setState(controlQuestionCount >= TOTAL_PROFILE_QUESTIONS_COUNT ? ProfileFillState.FULL : ProfileFillState.PARTIAL);
        response.setLastConversationMessageId(lastConversationMessageId);
        response.setProfile(profile);
        System.out.println(">> PROFILING FINISHED. Questions answered:" + controlQuestionCount
                + " Fill state: " + response.getState());
        return response;
    }

    @Override
    public DaiVinchikProfileGenerator getProfileGenerator() {
        return profileGenerator;
    }

}
