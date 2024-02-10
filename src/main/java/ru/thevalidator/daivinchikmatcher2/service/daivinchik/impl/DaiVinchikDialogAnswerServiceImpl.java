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
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard.KeyboardButton;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard.KeyboardButtonAction;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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
        return getAnswerTextByCase(type, data);
    }

    private DaiVinchikDialogAnswer getAnswerTextByCase(CaseType type, MessageAndKeyboard data) {
        DaiVinchikDialogAnswer answer;

        KeyboardButtonAction action;
        if (type.equals(CaseType.PROFILE)) {
            action = getActionForProfile(data);
            answer = createAnswer(type, action);
        } else if (type.equals(CaseType.ONE_BUTTON_ANSWER)) {
            action = data.getKeyboard().getButtons().get(0).get(0).getAction();
            answer = createAnswer(type, action);
        } else if (type.equals(CaseType.LOCATION)) {
            action = data.getKeyboard().getButtons().get(1).get(0).getAction();
            answer = createAnswer(type, action);
        } else if (type.equals(CaseType.QUESTION_AFTER_PROFILE)) {
            answer = getAnswerForThePreviousMessage(data);
        } else if (type.equals(CaseType.LONG_TIME_AWAY)) {
            action = data.getKeyboard().getButtons().get(0).get(0).getAction();
            answer = createAnswer(type, action);
        } else if (type.equals(CaseType.SHOW_QUESTION)) {
            action = data.getKeyboard().getButtons().get(0).get(0).getAction();
            answer = createAnswer(type, action);
        } else if (type.equals(CaseType.WANT_TO_MEET)) {
            action = data.getKeyboard().getButtons().get(0).get(0).getAction();
            answer = createAnswer(type, action);
        } else if (type.equals(CaseType.ADS_DV)) {
            action = data.getKeyboard().getButtons().get(0).get(1).getAction();
            answer = createAnswer(type, action);
        } else if (type.equals(CaseType.ADVICE)) {
            action = data.getKeyboard().getButtons().get(0).get(1).getAction();
            answer = createAnswer(type, action);
        } else if (type.equals(CaseType.QUESTION)) {
            action = data.getKeyboard().getButtons().get(0).get(0).getAction();
            answer = createAnswer(type, action);
        } else if (type.equals(CaseType.SLEEPING)) {
            action = data.getKeyboard().getButtons().get(0).get(0).getAction();
            answer = createAnswer(type, action);
        } else if (type.equals(CaseType.PROFILE_LIKED_ME)) {
            action = data.getKeyboard().getButtons().get(0).get(0).getAction();
            answer = createAnswer(type, action);
        } else if (type.equals(CaseType.SOMEBODY_LIKES_YOU)) {
            action = data.getKeyboard().getButtons().get(0).get(1).getAction();
            answer = createAnswer(type, action);
        } else if (type.equals(CaseType.DISABLE_PROFILE_QUESTION)) {
            action = data.getKeyboard().getButtons().get(0).get(0).getAction();
            answer = createAnswer(type, action);
        } else if (type.equals(CaseType.TOO_MANY_LIKES)) {
            throw new TooManyLikesForToday();
        } else if (type.equals(CaseType.CAN_NOT_CONTINUE)) {
            throw new CanNotContinueException(data);
        } else {
            answer = getAnswerFromUser(Thread.currentThread().getName(), type, data);
        }
        return answer;
        //@TODO: answer-duplication case
        //@TODO: add question after inactive
    }

    private DaiVinchikDialogAnswer createAnswer(CaseType type, KeyboardButtonAction action) {
        return new DaiVinchikDialogAnswer(action.getLabel(), action.getPayload(), type);
    }

    private DaiVinchikDialogAnswer getAnswerFromUser(String threadName, CaseType type, MessageAndKeyboard data) {
        Integer buttonNumber = null;
        int result = JOptionPane.CANCEL_OPTION;

        StringBuilder sb = new StringBuilder(data.getMessage().getText() + "\n");
        List<KeyboardButton> buttons = new ArrayList<>();
        if (data.getKeyboard() != null) {
            int counter = 0;
            for (List<KeyboardButton> buttonRow: data.getKeyboard().getButtons()) {
                for (KeyboardButton button: buttonRow) {
                    counter++;
                    sb.append("\n [ BUTTON №")
                            .append(counter)
                            .append(" ] - ");

                    if (button.getAction().getLabel() != null) {
                        sb.append("Label: ")
                                .append(button.getAction().getLabel());
                    }

                    if (button.getAction().getPayload() != null) {
                        sb.append(" Payload: ")
                                .append(button.getAction().getPayload());
                    }
                    buttons.add(button);
                }
            }
        }
        String messageInfo = sb.toString();
        while (result != JOptionPane.OK_OPTION || buttonNumber == null) {
            JTextArea jTextArea = new JTextArea(messageInfo);
            JTextField answerField = new JTextField();

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("MESSAGE:"));
            panel.add(jTextArea);
            panel.add(new JLabel("GIVE ANSWER  (button number or 0 to stop this account):"));
            panel.add(answerField);

            result = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    threadName,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    if (answerField.getText().trim().equals("0")) {
                        throw new CanNotContinueException("Stopped manually during getting answer from user");
                    }
                    buttonNumber = Integer.parseInt(answerField.getText().trim()) - 1;
                    if (buttonNumber < 0 || buttonNumber >= buttons.size()) {
                        buttonNumber = null;
                    }
                } catch (NumberFormatException ignored){
                }
            }
        }
        var action = buttons.get(buttonNumber).getAction();

        return new DaiVinchikDialogAnswer(action.getLabel(), action.getPayload(), type);
    }

    private KeyboardButtonAction getActionForProfile(MessageAndKeyboard data) {
        KeyboardButtonAction action = data.getKeyboard().getButtons().get(0).get(2).getAction();
        boolean isMatches = false;
        for (String s: data.getMessage().getText().split("\\s")) {
            s = s.toLowerCase();
            for (String w: matchingWords) {
                if (s.contains(w)) {
                    action = data.getKeyboard().getButtons().get(0).get(0).getAction();
                    LOG.info("Match on word {}", w);
                    isMatches = true;
                    break;
                }
            }
            if (isMatches) {
                break;
            }
        }
        return action;
    }

    private DaiVinchikDialogAnswer getAnswerForThePreviousMessage(MessageAndKeyboard data) {
        LOG.debug("Searching the answer for previous question");
        Message message = messageService.findMessageFromDaiVinchikBefore(data.getMessage());
        data.setMessage(message);
        CaseType caseType = matcher.detectCase(data);
        LOG.debug("Returning the answer for previous question");
        return getAnswerTextByCase(caseType, data);
    }

}
