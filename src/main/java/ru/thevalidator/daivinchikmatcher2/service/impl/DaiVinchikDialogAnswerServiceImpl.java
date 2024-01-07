package ru.thevalidator.daivinchikmatcher2.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.thevalidator.daivinchikmatcher2.service.CaseMatcher;
import ru.thevalidator.daivinchikmatcher2.service.CaseType;
import ru.thevalidator.daivinchikmatcher2.service.DaiVinchikDialogAnswerService;
import ru.thevalidator.daivinchikmatcher2.vk.dto.DaiVinchikDialogAnswer;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;

import java.util.Scanner;

public class DaiVinchikDialogAnswerServiceImpl implements DaiVinchikDialogAnswerService {

    private static final Logger LOG = LoggerFactory.getLogger(DaiVinchikDialogAnswerServiceImpl.class);
    private final CaseMatcher matcher;
    private int counter = 0;
    private int lastMessageId = 0;

    public DaiVinchikDialogAnswerServiceImpl() {
        matcher = new CaseMatcherImpl();
    }

    public DaiVinchikDialogAnswerServiceImpl(CaseMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public DaiVinchikDialogAnswer findAnswer(MessageAndKeyboard data) {
        LOG.info("Iteration: {}, missed {}", ++counter, (data.getMessage().getConversationMessageId() - lastMessageId + 2)); //@TODO: transfer to main service class
        lastMessageId = data.getMessage().getConversationMessageId();
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
        } else {
            LOG.debug("UNKNOWN STATE: {}", data);
            System.out.println("Кто-то хочет познакомиться");
            System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" +
                    "\nMESSAGE: " + data.getMessage().getText());
            System.out.println("\nKEYBOARD: " + data.getKeyboard().getButtons());
            Scanner sc = new Scanner(System.in);
            System.out.print("\n>>>>\tENTER CORRECT ANSWER:");
            String input = sc.nextLine();
            System.out.println();
            LOG.debug("STATE ANSWER: {}", input);
            answer.setText(input);
        }

        return answer;
    }

}
