package ru.thevalidator.daivinchikmatcher2.service.impl;

import com.vk.api.sdk.objects.messages.KeyboardButtonActionLocationType;
import com.vk.api.sdk.objects.messages.KeyboardButtonActionTextType;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.api.sdk.objects.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.thevalidator.daivinchikmatcher2.exception.CanNotContinueException;
import ru.thevalidator.daivinchikmatcher2.service.CaseMatcher;
import ru.thevalidator.daivinchikmatcher2.service.CaseType;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard.Keyboard;
import ru.thevalidator.daivinchikmatcher2.vk.dto.dupl.message.conversation.keyboard.KeyboardButton;

import java.util.List;
import java.util.Objects;

@Component
public class CaseMatcherImpl implements CaseMatcher {

    private static final Logger LOG = LoggerFactory.getLogger(CaseMatcherImpl.class);
    private static final String PROFILE_REGEXP = "(?<name>([\\p{L}\\p{N}\\p{P}\\p{Z}\\W$^+=|`~№]+)?,) " +
                    "(?<age>\\d{1,3},) " +
                    "(?<city>[\\p{L}\\p{N}\\p{P}\\p{Z}$^+=|`~№]+)" +
                    "(?<text>(((<br>)|\\n)*.*)*)";


    //@TODO: move sympathy checking here or not ???
    public boolean isSympathyKeyboardPattern(com.vk.api.sdk.objects.messages.Keyboard keyboard) {
        return keyboard != null
                && keyboard.getInline()
                && keyboard.getButtons().size() == 1
                && keyboard.getButtons().get(0).size() == 1;
    }

    @Override
    public CaseType detectCase(MessageAndKeyboard data) {
        CaseType type;

        LOG.trace("MESSAGE: {}", data);
        if (Objects.isNull(data.getKeyboard())) {
            throw new CanNotContinueException(data);
        }

        if (isProfile(data)) {
            type = CaseType.PROFILE;
            //} else if (isWarning(data)) {
            //    type = CaseType.WARNING;
        } else if (isOneButton(data)) {
            type = CaseType.ONE_BUTTON_ANSWER;
        } else if (isLocation(data)) {
            type = CaseType.LOCATION;
        } else if (isAdsDV(data)) {
            type = CaseType.ADS_DV;
        } else if (isAdvice(data)) {
            type = CaseType.ADVICE;
        } else if (isQuestion(data)) {
            type = CaseType.QUESTION;
        } else if (isWantToMeet(data)) {
            type = CaseType.WANT_TO_MEET;
        } else if (hasLikeFromSomeone(data)) {
            type = CaseType.SOMEBODY_LIKES_YOU;
        } else if (isQuestionAfterProfile(data)) {
            type = CaseType.QUESTION_AFTER_PROFILE;
        } else if (isSleeping(data)) {
            type = CaseType.SLEEPING;
        } else if (isShowQuestion(data)) {
            type = CaseType.SHOW_QUESTION;
        } else if (isProfileLikedMe(data)) {
            type = CaseType.PROFILE_LIKED_ME;
        } else if (isProfileUrl(data)) {
            type = CaseType.PROFILE_URL;
        } else if (isTooMuchForToday(data)) {
            type = CaseType.TOO_MANY_LIKES;
        } else if (isLongTimeAway(data)) {
            type = CaseType.LONG_TIME_AWAY;
        } else if (isNoSuchAnswer(data)) {
            type = CaseType.NO_SUCH_ANSWER;
        }


        //@TODO: check for captcha
//
//        else if (hasKeyboardSkipButton(data.getKeyboard())) {
//            type = CaseType.SKIP_BUTTON;
//        }
//
//        else if (hasMessageSkipText(data.getMessage())) {
//            type = CaseType.SKIP_TEXT;
//        }


        else {
            type = CaseType.UNKNOWN;
            LOG.debug("{} MESSAGE TYPE: {}", type, data);
        }

        LOG.trace("TYPE: {}", type);
        return type;
    }

    public boolean isProfile(MessageAndKeyboard data) {
        Message message = data.getMessage();
        Keyboard keyboard = data.getKeyboard();
        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();
        return !keyboard.getOneTime()
                && buttonRows.size() == 1
                && buttonRows.get(0).size() == 4
                && buttonRows.get(0).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.POSITIVE.getValue())
                && buttonRows.get(0).get(3).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(3).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && !message.getText().startsWith("Кому-то понравилась твоя анкета")
                && message.getText().matches(PROFILE_REGEXP);
    }

    public boolean isProfileLikedMe(MessageAndKeyboard data) {
        Message message = data.getMessage();
        Keyboard keyboard = data.getKeyboard();
        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();
        return !keyboard.getOneTime()
                && buttonRows.size() == 1
                && buttonRows.get(0).size() == 4
                && buttonRows.get(0).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.POSITIVE.getValue())
                && buttonRows.get(0).get(3).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(3).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && message.getText().startsWith("Кому-то понравилась твоя анкета")
                && message.getText().matches(PROFILE_REGEXP);
    }

    public boolean isWarning(MessageAndKeyboard data) {
        Keyboard keyboard = data.getKeyboard();
        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();
        return !keyboard.getOneTime()
                && buttonRows.size() == 1
                && buttonRows.get(0).size() == 1
                && buttonRows.get(0).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(0).getAction().getLabel().equals("Продолжить просмотр анкет")
                && buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.DEFAULT.getValue());
    }

    public boolean isOneButton(MessageAndKeyboard data) {
        Keyboard keyboard = data.getKeyboard();
        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();
        return !keyboard.getOneTime()
                && buttonRows.size() == 1
                && buttonRows.get(0).size() == 1
                && buttonRows.get(0).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue());
        //&& buttonRows.get(0).get(0).getAction().getLabel().equals("Продолжить просмотр анкет")
        //&& buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.POSITIVE.getValue());
    }

    public boolean isLocation(MessageAndKeyboard data) {
        Keyboard keyboard = data.getKeyboard();
        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();
        return !keyboard.getOneTime()
                && buttonRows.size() == 2
                && buttonRows.get(0).size() == 1
                && buttonRows.get(0).get(0).getAction().getType().equals(KeyboardButtonActionLocationType.LOCATION.getValue())
                && buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && buttonRows.get(1).size() == 1
                && buttonRows.get(1).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(1).get(0).getAction().getLabel().equals("Продолжить просмотр анкет")
                && buttonRows.get(1).get(0).getColor().equals(KeyboardButtonColor.DEFAULT.getValue());

    }

    public boolean isAdsDV(MessageAndKeyboard data) {
        Keyboard keyboard = data.getKeyboard();
        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();
        return !keyboard.getOneTime()
                && buttonRows.size() == 1
                && buttonRows.get(0).size() == 2
                && buttonRows.get(0).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(0).getAction().getLabel().equals("Анкеты в Telegram")
                && buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.POSITIVE.getValue())
                && buttonRows.get(0).get(1).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(1).getAction().getLabel().equals("Анкеты в VK")
                && buttonRows.get(0).get(1).getColor().equals(KeyboardButtonColor.DEFAULT.getValue());
    }

    public boolean isAdvice(MessageAndKeyboard data) {
        Message message = data.getMessage();
        Keyboard keyboard = data.getKeyboard();
        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();
        return !keyboard.getOneTime()
                && buttonRows.size() == 1
                && buttonRows.get(0).size() == 2
                && buttonRows.get(0).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.POSITIVE.getValue())
                && buttonRows.get(0).get(1).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(1).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && !message.getText().contains("Ты понравил");   //@TODO: improve text check conditions;
    }

    public boolean isQuestion(MessageAndKeyboard data) {
        Message message = data.getMessage();
        Keyboard keyboard = data.getKeyboard();
        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();
        return !keyboard.getOneTime()
                && buttonRows.size() == 2
                && buttonRows.get(0).size() == 1
                && buttonRows.get(1).size() == 1
                && buttonRows.get(0).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.POSITIVE.getValue())
                && buttonRows.get(1).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(1).get(0).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && !message.getText().contains("Ты понравил");   //@TODO: improve text check conditions;
    }

    public boolean isTooMuchForToday(MessageAndKeyboard data) {
        Message message = data.getMessage();
        Keyboard keyboard = data.getKeyboard();
        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();
        return !keyboard.getOneTime()
                && buttonRows.size() == 1
                && buttonRows.get(0).size() == 4
                && buttonRows.get(0).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.POSITIVE.getValue())
                && buttonRows.get(0).get(1).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(1).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && buttonRows.get(0).get(2).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(2).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && buttonRows.get(0).get(3).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(3).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && message.getText().startsWith("Слишком много лайков за сегодня")
                && message.getText().endsWith("Бот знакомств Дайвинчик в Telegram.");
    }

    public boolean isWantToMeet(MessageAndKeyboard data) {
        Message message = data.getMessage();
        Keyboard keyboard = data.getKeyboard();
        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();//@TODO: improve text check conditions
        return keyboard.getOneTime()
                && buttonRows.size() == 1
                && buttonRows.get(0).size() == 2
                && buttonRows.get(0).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.POSITIVE.getValue())
                && buttonRows.get(0).get(1).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(1).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && message.getText().contains("с тобой");//"Герман, 9 девушек из г. Москва хотят пообщаться с тобой.\n\n1. Посмотреть их анкеты.\n2. Моя анкета."
    }

    public boolean hasLikeFromSomeone(MessageAndKeyboard data) {
        Message message = data.getMessage();
        Keyboard keyboard = data.getKeyboard();
        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();//@TODO: improve text check conditions
        return !keyboard.getOneTime()
                && buttonRows.size() == 1
                && buttonRows.get(0).size() == 2
                && buttonRows.get(0).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.POSITIVE.getValue())
                && buttonRows.get(0).get(1).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(1).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && message.getText().contains("показать");//"Ты понравился 1 человеку, показать его?"
    }

    private boolean isQuestionAfterProfile(MessageAndKeyboard data) {
        Message message = data.getMessage();
//        Keyboard keyboard = data.getKeyboard();
//        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();
//        return !keyboard.getOneTime()
//                && buttonRows.size() == 1
//                && buttonRows.get(0).size() == 2
//                && buttonRows.get(0).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
//                && buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.POSITIVE.getValue())
//                && buttonRows.get(0).get(1).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
//                && buttonRows.get(0).get(1).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
//                && message.getText().contains("Заканчивай с вопросом выше");
                return message.getText().contains("Заканчивай с вопросом выше");
        //"Нашли кое-кого для тебя ;) Заканчивай с вопросом выше и увидишь кто это"
    }

    private boolean isNoSuchAnswer(MessageAndKeyboard data) {
        Message message = data.getMessage();
                return message.getText().contains("Нет такого варианта ответа");
        //"Нет такого варианта ответа"
    }

    public boolean isSleeping(MessageAndKeyboard data) {
        Message message = data.getMessage();
        Keyboard keyboard = data.getKeyboard();
        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();
        return !keyboard.getOneTime()
                && buttonRows.size() == 1
                && buttonRows.get(0).size() == 4
                && buttonRows.get(0).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.POSITIVE.getValue())
                && buttonRows.get(0).get(1).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(1).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && buttonRows.get(0).get(2).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(2).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && buttonRows.get(0).get(3).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(3).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && message.getText().startsWith("1. Смотреть анкеты.")
                && message.getText().endsWith("Бот знакомств Дайвинчик в Telegram.");
    }

    public boolean isShowQuestion(MessageAndKeyboard data) {
        Message message = data.getMessage();
        Keyboard keyboard = data.getKeyboard();
        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();
        return !keyboard.getOneTime()
                && buttonRows.size() == 1
                && buttonRows.get(0).size() == 3
                && buttonRows.get(0).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.POSITIVE.getValue())
                && buttonRows.get(0).get(1).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(1).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && buttonRows.get(0).get(2).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(2).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && message.getText().startsWith("1. Показать")
                && message.getText().endsWith("3. Я больше не хочу никого искать.");
    }

    public boolean isLongTimeAway(MessageAndKeyboard data) {
        Message message = data.getMessage();
        Keyboard keyboard = data.getKeyboard();
        List<List<KeyboardButton>> buttonRows = keyboard.getButtons();
        return keyboard.getOneTime()
                && buttonRows.size() == 1
                && buttonRows.get(0).size() == 3
                && buttonRows.get(0).get(0).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(0).getColor().equals(KeyboardButtonColor.POSITIVE.getValue())
                && buttonRows.get(0).get(1).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(1).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && buttonRows.get(0).get(2).getAction().getType().equals(KeyboardButtonActionTextType.TEXT.getValue())
                && buttonRows.get(0).get(2).getColor().equals(KeyboardButtonColor.DEFAULT.getValue())
                && message.getText().contains("анкета больше не участвует в поиске");
        //&& message.getText().endsWith("3. Я больше никого не ищу.");
    }

    public boolean isProfileUrl(MessageAndKeyboard data) {
        Message message = data.getMessage();

        return message.getText().startsWith("Отлично! Надеюсь хорошо проведете время")
                || message.getText().contains("добавляй в друзья -");
    }


    //"Есть взаимная симпатия! Добавляй в друзья - vk.com/id144149953\n\nОлег, 25, Москва"
    //"Есть взаимная симпатия! Добавляй в друзья - vk.com/id751006895\n\nвикуся, 15, Москва\nищу так чисто мальчика любимого"


    public boolean notAvailableToContinue(MessageAndKeyboard message) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    public boolean hasKeyboardSkipButton(Keyboard keyboard) {
        if (keyboard == null) return false;
        for (List<KeyboardButton> buttons: keyboard.getButtons()) {
            for (KeyboardButton b: buttons) {
                if (isSkipButton(b)) {
                    throw new UnsupportedOperationException("Not supported yet");
                    //return true;
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

//    private boolean isProfileKeyboard(Keyboard keyboard) {
//            List<KeyboardButton> buttons = keyboard.getButtons().get(0);
//            return (keyboard.getButtons().size() == 1)
//                    && KeyboardButtonColor.POSITIVE.getValue().equals(buttons.get(0).getColor())
//                    && KeyboardButtonColor.DEFAULT.getValue().equals(buttons.get(3).getColor());
//    }

}
