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

    @Override
    public CaseType detectCase(MessageAndKeyboard message) {
        CaseType type = null;

        //@TODO: check for captcha

        if (notAvailableToContinue(message)) {
            type = CaseType.CAN_NOT_CONTINUE;
        }

        else if (hasKeyboardSkipButton(message.getKeyboard())) {
            type = CaseType.SKIP_BUTTON;
        }

        else if (hasMessageSkipText(message.getMessage())) {
            type = CaseType.SKIP_TEXT;
        }

        else if (isProfile(message)) {
            type = CaseType.PROFILE;
        }


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

    public boolean isProfile(MessageAndKeyboard message) {
        if (isProfileKeyboard(message.getKeyboard())) {

        }

        throw new UnsupportedOperationException("Not supported yet");
    }

    private boolean isProfileKeyboard(Keyboard keyboard) {
            List<KeyboardButton> buttons = keyboard.getButtons().get(0);
            return (keyboard.getButtons().size() == 1)
                    && KeyboardButtonColor.POSITIVE.getValue().equals(buttons.get(0).getColor())
                    && KeyboardButtonColor.DEFAULT.getValue().equals(buttons.get(3).getColor());
    }

}
