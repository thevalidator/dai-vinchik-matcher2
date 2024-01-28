package ru.thevalidator.daivinchikmatcher2.service.daivinchik.impl;

import com.vk.api.sdk.objects.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.thevalidator.daivinchikmatcher2.exception.CanNotContinueException;
import ru.thevalidator.daivinchikmatcher2.exception.TooManyLikesForToday;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikCaseMatcherService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikDialogAnswerService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.DaiVinchikMessageService;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.CaseType;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.DaiVinchikDialogAnswer;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;

import java.util.Scanner;
import java.util.Set;

public class DaiVinchikDialogAnswerServiceImpl implements DaiVinchikDialogAnswerService {

    private static final Logger LOG = LoggerFactory.getLogger(DaiVinchikDialogAnswerServiceImpl.class);
    private final DaiVinchikCaseMatcherService matcher;
    private final DaiVinchikMessageService messageService;
    private final Set<String> matchingWords;

    public DaiVinchikDialogAnswerServiceImpl(DaiVinchikCaseMatcherService matcher,
                                             DaiVinchikMessageService messageService,
                                             Set<String> matchingWords) {
        this.matcher = matcher;
        this.messageService = messageService;
        this.matchingWords = matchingWords;
    }

    @Override
    public DaiVinchikDialogAnswer findAnswer(MessageAndKeyboard data) {
        CaseType type = matcher.detectCase(data);
        LOG.debug("{}", type);
        String answerText = getAnswerTextByCase(type, data);
        DaiVinchikDialogAnswer answer = new DaiVinchikDialogAnswer();
        answer.setType(type);
        answer.setText(answerText);
        return answer;
    }

    private String getAnswerTextByCase(CaseType type, MessageAndKeyboard data) {
        String text;
        if (type.equals(CaseType.PROFILE)) {
            text = getAnswerForProfile(data);
        } else if (type.equals(CaseType.ONE_BUTTON_ANSWER)) {
            text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
        } else if (type.equals(CaseType.LOCATION)) {
            text = data.getKeyboard().getButtons().get(1).get(0).getAction().getPayload();
        } else if (type.equals(CaseType.QUESTION_AFTER_PROFILE)) {
            text = getAnswerForThePreviousMessage(data);
        } else if (type.equals(CaseType.NO_SUCH_ANSWER)) {
            var b = data.getKeyboard().getButtons();
            text = b.size() == 1 ? b.get(0).get(0).getAction().getPayload()
                    : b.get(1).get(0).getAction().getLabel();
        } else if (type.equals(CaseType.LONG_TIME_AWAY)) {
            text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
        } else if (type.equals(CaseType.SHOW_QUESTION)) {
            text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
        } else if (type.equals(CaseType.WANT_TO_MEET)) {
            text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
        } else if (type.equals(CaseType.ADS_DV)) {
            text = data.getKeyboard().getButtons().get(0).get(1).getAction().getPayload();
        } else if (type.equals(CaseType.ADVICE)) {
            text = data.getKeyboard().getButtons().get(0).get(1).getAction().getPayload();
        } else if (type.equals(CaseType.QUESTION)) {
            text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
        } else if (type.equals(CaseType.SLEEPING)) {
            text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
        } else if (type.equals(CaseType.PROFILE_LIKED_ME)) {
            //@TODO: check if the answer is correct
            text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
        } else if (type.equals(CaseType.SOMEBODY_LIKES_YOU)) {
            //@TODO: check if the answer is correct
            text = data.getKeyboard().getButtons().get(0).get(1).getAction().getPayload();
        } else if (type.equals(CaseType.DISABLE_PROFILE_QUESTION)) {
            text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
        }else if (type.equals(CaseType.TOO_MANY_LIKES)) {
            throw new TooManyLikesForToday(); //@TODO: move to the message handler  ???
        } else if (type.equals(CaseType.CAN_NOT_CONTINUE)) {
            throw new CanNotContinueException(data); //@TODO: move to the message handler ???
        } else {
            System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" +
                    "\nMESSAGE: " + data.getMessage().getText());
            System.out.println("\nKEYBOARD: " + data.getKeyboard().getButtons());
            Scanner sc = new Scanner(System.in);
            System.out.print("\n>>>>\tENTER CORRECT ANSWER:");
            text = sc.nextLine();
            System.out.println();
        }
        return text;
        //@TODO: answer-duplication case
        //@TODO: add question after inactive
    }

    private String getAnswerForProfile(MessageAndKeyboard data) {
        String text = data.getKeyboard().getButtons().get(0).get(2).getAction().getPayload();
        boolean isMatches = false;
        for (String s: data.getMessage().getText().split("\\s")) {
            s = s.toLowerCase();
            for (String w: matchingWords) {
                if (s.contains(w)) {
                    text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
                    LOG.info("Match on word {}", w);
                    isMatches = true;
                    break;
                }
            }
            if (isMatches) {
                break;
            }
        }
        return text;
    }

    private String getAnswerForThePreviousMessage(MessageAndKeyboard data) {
        LOG.debug("Searching the answer for previous question");
        Message message = messageService.findMessageFromDaiVinchikBefore(data.getMessage());
        data.setMessage(message);
        CaseType caseType = matcher.detectCase(data);
        LOG.debug("Returning the answer for previous question");
        return getAnswerTextByCase(caseType, data);
    }

}
