package ru.thevalidator.daivinchikmatcher2.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.thevalidator.daivinchikmatcher2.exception.CanNotContinueException;
import ru.thevalidator.daivinchikmatcher2.exception.TooManyLikesForToday;
import ru.thevalidator.daivinchikmatcher2.service.CaseMatcher;
import ru.thevalidator.daivinchikmatcher2.service.CaseType;
import ru.thevalidator.daivinchikmatcher2.service.DaiVinchikDialogAnswerService;
import ru.thevalidator.daivinchikmatcher2.vk.dto.DaiVinchikDialogAnswer;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;

import java.util.Scanner;

@Component
public class DaiVinchikDialogAnswerServiceImpl implements DaiVinchikDialogAnswerService {

    private static final Logger LOG = LoggerFactory.getLogger(DaiVinchikDialogAnswerServiceImpl.class);
    private final CaseMatcher matcher;

    @Autowired
    public DaiVinchikDialogAnswerServiceImpl(CaseMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public DaiVinchikDialogAnswer findAnswer(MessageAndKeyboard data) {
        DaiVinchikDialogAnswer answer = new DaiVinchikDialogAnswer();
        CaseType type = matcher.detectCase(data);
        if (type.equals(CaseType.PROFILE)) {
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            //String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getLabel();
            answer.setText(text);
        } else if (type.equals(CaseType.WARNING) || type.equals(CaseType.ONE_BUTTON_ANSWER)) {
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.LOCATION)) {
            String text = data.getKeyboard().getButtons().get(1).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.ADS_DV)) {
            String text = data.getKeyboard().getButtons().get(0).get(1).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.ADVICE)) {
            String text = data.getKeyboard().getButtons().get(0).get(1).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.SLEEPING)) {
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.PROFILE_LIKED_ME)) {
            //@TODO: check if the answer is correct
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.SOMEBODY_LIKES_YOU)) {
            //@TODO: check if the answer is correct
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.TOO_MANY_LIKES)) {
            //@TODO: check record in logs without this log
            LOG.debug("TOO MANY LIKES: {}", data);
            throw new TooManyLikesForToday();
        } else if (type.equals(CaseType.CAN_NOT_CONTINUE)) {
            //@TODO: check record in logs without this log
            LOG.debug("CAN NOT CONTINUE: {}", data);
            throw new CanNotContinueException(data);
        } else {
            System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" +
                    "\nMESSAGE: " + data.getMessage().getText());
            System.out.println("\nKEYBOARD: " + data.getKeyboard().getButtons());
            Scanner sc = new Scanner(System.in);
            System.out.print("\n>>>>\tENTER CORRECT ANSWER:");
            String input = sc.nextLine();
            System.out.println();
            answer.setText(input);
        }

        return answer;
    }

}
