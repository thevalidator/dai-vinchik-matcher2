package ru.thevalidator.daivinchikmatcher2.service;

import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;

public interface CaseMatcher {
    public CaseType detectCase(MessageAndKeyboard message);
}
