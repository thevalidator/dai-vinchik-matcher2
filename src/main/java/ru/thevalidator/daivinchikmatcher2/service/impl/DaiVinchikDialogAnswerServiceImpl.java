package ru.thevalidator.daivinchikmatcher2.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.thevalidator.daivinchikmatcher2.exception.CanNotContinueException;
import ru.thevalidator.daivinchikmatcher2.exception.TooManyLikesForToday;
import ru.thevalidator.daivinchikmatcher2.service.CaseMatcher;
import ru.thevalidator.daivinchikmatcher2.service.CaseType;
import ru.thevalidator.daivinchikmatcher2.service.DaiVinchikDialogAnswerService;
import ru.thevalidator.daivinchikmatcher2.vk.dto.DaiVinchikDialogAnswer;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;

import java.util.Scanner;

public class DaiVinchikDialogAnswerServiceImpl implements DaiVinchikDialogAnswerService {

    private static final Logger LOG = LoggerFactory.getLogger(DaiVinchikDialogAnswerServiceImpl.class);
    private final CaseMatcher matcher;

    public DaiVinchikDialogAnswerServiceImpl() {
        matcher = new CaseMatcherImpl();
    }

    public DaiVinchikDialogAnswerServiceImpl(CaseMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public DaiVinchikDialogAnswer findAnswer(MessageAndKeyboard data) {
        DaiVinchikDialogAnswer answer = new DaiVinchikDialogAnswer();
        CaseType type = matcher.detectCase(data);
        if (type.equals(CaseType.PROFILE)) {
            LOG.debug("PROFILE: {}", data);
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.WARNING)) {
            LOG.debug("WARNING: {}", data);
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.LOCATION)) {
            LOG.debug("LOCATION: {}", data);
            String text = data.getKeyboard().getButtons().get(1).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.ADS_DV)) {
            LOG.debug("ADS_DV: {}", data);
            String text = data.getKeyboard().getButtons().get(0).get(1).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.ADVICE)) {
            LOG.debug("ADVICE: {}", data);
            String text = data.getKeyboard().getButtons().get(0).get(1).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.SLEEPING)) {
            LOG.debug("SLEEPING: {}", data);
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.PROFILE_LIKED_ME)) {
            LOG.debug("PROFILE LIKED ME: {}", data);
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.TOO_MANY_LIKES)) {
            LOG.debug("TOO MANY LIKES: {}", data);
            throw new TooManyLikesForToday();
        } else if (type.equals(CaseType.CAN_NOT_CONTINUE)) {
            LOG.debug("CAN NOT CONTINUE: {}", data);
            throw new CanNotContinueException(data);
        } else {
            LOG.debug("UNKNOWN STATE: {}", data);
            System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" +
                    "\nMESSAGE: " + data.getMessage().getText());
            System.out.println("\nKEYBOARD: " + data.getKeyboard().getButtons());
            Scanner sc = new Scanner(System.in);
            System.out.print("\n>>>>\tENTER CORRECT ANSWER:");
            String input = sc.nextLine();
            System.out.println();
            answer.setText(input);
        }
        LOG.debug("{} STATE ANSWER: {}", type, answer.getText());

        return answer;
    }

}
