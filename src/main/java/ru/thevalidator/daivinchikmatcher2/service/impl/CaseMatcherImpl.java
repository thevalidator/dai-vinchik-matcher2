package ru.thevalidator.daivinchikmatcher2.service.impl;

import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.api.sdk.objects.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;
import ru.thevalidator.daivinchikmatcher2.service.CaseType;
import ru.thevalidator.daivinchikmatcher2.service.CaseMatcher;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard.Keyboard;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard.KeyboardButton;

import java.util.List;

public class CaseMatcherImpl implements CaseMatcher {

    private static final Logger LOG = LoggerFactory.getLogger(CaseMatcherImpl.class);
    private static final String PROFILE_REGEXP = //"([\\p{L}\\p{N}\\p{P}\\p{Z}\\W$^+=|`~№]+(<br>|\\n)+)?" +
            "(?<name>([\\p{L}\\p{N}\\p{P}\\p{Z}\\W$^+=|`~№]+)?,) " +
            "(?<age>\\d{1,3},) " +
            "(?<city>[\\p{L}\\p{N}\\p{P}\\p{Z}$^+=|`~№]+)" +
            "(?<text>(((<br>)|\\n)+.*)*)";


    @Override
    public CaseType detectCase(MessageAndKeyboard message) {
        CaseType type = null;

        if (isProfile(message)) {
            type = CaseType.PROFILE;
        }

        //@TODO: check for captcha

//        if (notAvailableToContinue(message)) {
//            type = CaseType.CAN_NOT_CONTINUE;
//        }
//
//        else if (hasKeyboardSkipButton(message.getKeyboard())) {
//            type = CaseType.SKIP_BUTTON;
//        }
//
//        else if (hasMessageSkipText(message.getMessage())) {
//            type = CaseType.SKIP_TEXT;
//        }
//
//        else if (isProfile(message)) {
//            type = CaseType.PROFILE;
//        }


        else {
            type = CaseType.UNKNOWN;
        }

        LOG.info("Case type detected: {}", type);
        return type;
    }

    public boolean notAvailableToContinue(MessageAndKeyboard message) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    public boolean hasKeyboardSkipButton(Keyboard keyboard) {
        for (List<KeyboardButton> buttons: keyboard.getButtons()) {
            for (KeyboardButton b: buttons) {
                if (isSkipButton(b)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSkipButton(KeyboardButton b) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    public boolean hasMessageSkipText(Message message) {
        //if (text.contains("Смотреть анкеты")) {}
        throw new UnsupportedOperationException("Not supported yet");
    }

    public boolean isProfile(MessageAndKeyboard data) {
        Message message = data.getMessage();
        Keyboard keyboard = data.getKeyboard();
        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();
        return buttonRows.size() == 1
                && buttonRows.get(0).size() == 4
                && buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.POSITIVE.getValue())
                && buttonRows.get(0).get(3).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && message.getText().matches(PROFILE_REGEXP);

    }

//    private boolean isProfileKeyboard(Keyboard keyboard) {
//            List<KeyboardButton> buttons = keyboard.getButtons().get(0);
//            return (keyboard.getButtons().size() == 1)
//                    && KeyboardButtonColor.POSITIVE.getValue().equals(buttons.get(0).getColor())
//                    && KeyboardButtonColor.DEFAULT.getValue().equals(buttons.get(3).getColor());
//    }

}
