package ru.thevalidator.daivinchikmatcher2.service.daivinchik;

import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.DaiVinchikDialogAnswer;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;

public interface DaiVinchikDialogAnswerService {

    public DaiVinchikDialogAnswer findAnswer(MessageAndKeyboard data);

}
