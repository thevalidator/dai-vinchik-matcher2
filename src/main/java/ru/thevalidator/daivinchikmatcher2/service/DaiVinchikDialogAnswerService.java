package ru.thevalidator.daivinchikmatcher2.service;

import ru.thevalidator.daivinchikmatcher2.vk.dto.DaiVinchikDialogAnswer;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageWithKeyboard;

public interface DaiVinchikDialogAnswerService {

    public DaiVinchikDialogAnswer findAnswer(MessageWithKeyboard message);

}
