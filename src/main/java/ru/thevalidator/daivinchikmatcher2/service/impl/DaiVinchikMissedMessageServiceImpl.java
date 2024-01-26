package ru.thevalidator.daivinchikmatcher2.service.impl;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.thevalidator.daivinchikmatcher2.service.DaiVinchikMissedMessageService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DaiVinchikMissedMessageServiceImpl implements DaiVinchikMissedMessageService {

    private static final Logger LOG = LoggerFactory.getLogger(DaiVinchikMissedMessageServiceImpl.class);
    private static final Pattern pattern = Pattern.compile("\\W+друзья - (?<url>vk.com/id\\d+)");

    @Override
    public void findSympathy(Message message) {
        if (isSympathyKeyboardPattern(message.getKeyboard())) {
            Matcher matcher = pattern.matcher(message.getText());
            if (matcher.find()) {
                LOG.debug("profile: {}", matcher.group("url"));
            }
        } else {
            //@TODO: remove in future if sympathy detection works fine
            LOG.debug("Unknown missed message type: {}", message);
        }
    }

    private boolean isSympathyKeyboardPattern(Keyboard keyboard) {
        return keyboard != null
                && keyboard.getInline()
                && keyboard.getButtons().size() == 1
                && keyboard.getButtons().get(0).size() == 1;
    }

}
