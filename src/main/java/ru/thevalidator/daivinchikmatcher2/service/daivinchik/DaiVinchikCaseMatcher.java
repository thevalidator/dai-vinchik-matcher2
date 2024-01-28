package ru.thevalidator.daivinchikmatcher2.service.daivinchik;

import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.CaseType;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;

public interface DaiVinchikCaseMatcher {
    public CaseType detectCase(MessageAndKeyboard message);
}
