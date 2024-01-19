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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

@Component
public class DaiVinchikDialogAnswerServiceImpl implements DaiVinchikDialogAnswerService {

    private static final Logger LOG = LoggerFactory.getLogger(DaiVinchikDialogAnswerServiceImpl.class);
    private final CaseMatcher matcher;
    private Set<String> words;

    @Autowired
    public DaiVinchikDialogAnswerServiceImpl(CaseMatcher matcher) {
        this.matcher = matcher;
        initWords();
    }

    private void initWords() {
        words = new HashSet<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("data/words.txt"),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
        }
        LOG.info("Loaded {} words for matching", words.size());
    }

    @Override
    public DaiVinchikDialogAnswer findAnswer(MessageAndKeyboard data) {
        DaiVinchikDialogAnswer answer = new DaiVinchikDialogAnswer();
        CaseType type = matcher.detectCase(data);
        if (type.equals(CaseType.PROFILE)) {
            LOG.debug("{}", CaseType.PROFILE);
            String text = data.getKeyboard().getButtons().get(0).get(2).getAction().getPayload();
            boolean isMatches = false;
            for (String s: data.getMessage().getText().split("\\s")) {
                s = s.toLowerCase();
                for (String w: words) {
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
//            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            //String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getLabel();
            answer.setText(text);
        } else if (type.equals(CaseType.ONE_BUTTON_ANSWER)) {
            LOG.debug("{}", CaseType.ONE_BUTTON_ANSWER);
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.LOCATION)) {
            LOG.debug("{}", CaseType.LOCATION);
            String text = data.getKeyboard().getButtons().get(1).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.QUESTION_AFTER_PROFILE)) {
            LOG.debug("{}", CaseType.QUESTION_AFTER_PROFILE); //@TODO: check profile for matching
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.NO_SUCH_ANSWER)) {
            LOG.debug("{}", CaseType.NO_SUCH_ANSWER);
            var b =data.getKeyboard().getButtons();
            String text = b.size() == 1 ? b.get(0).get(0).getAction().getPayload()
                    : b.get(1).get(0).getAction().getLabel();
            answer.setText(text);
        } else if (type.equals(CaseType.LONG_TIME_AWAY)) {
            LOG.debug("{}", CaseType.LONG_TIME_AWAY);
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.SHOW_QUESTION)) {
            LOG.debug("{}", CaseType.SHOW_QUESTION);
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.WANT_TO_MEET)) {
            LOG.debug("{}", CaseType.WANT_TO_MEET);
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.ADS_DV)) {
            LOG.debug("{}", CaseType.ADS_DV);
            String text = data.getKeyboard().getButtons().get(0).get(1).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.ADVICE)) {
            LOG.debug("{}", CaseType.ADVICE);
            String text = data.getKeyboard().getButtons().get(0).get(1).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.SLEEPING)) {
            LOG.debug("{}", CaseType.SLEEPING);
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.PROFILE_LIKED_ME)) {
            LOG.debug("{}", CaseType.PROFILE_LIKED_ME);
            //@TODO: check if the answer is correct
            String text = data.getKeyboard().getButtons().get(0).get(0).getAction().getPayload();
            answer.setText(text);
        } else if (type.equals(CaseType.SOMEBODY_LIKES_YOU)) {
            LOG.debug("{}", CaseType.SOMEBODY_LIKES_YOU);
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
