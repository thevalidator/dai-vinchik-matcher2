package ru.thevalidator.daivinchikmatcher2.service;

import ru.thevalidator.daivinchikmatcher2.vk.dto.DaiVinchikDialogAnswer;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;

public interface DaiVinchikDialogAnswerService {

    public DaiVinchikDialogAnswer findAnswer(MessageAndKeyboard message);

}
