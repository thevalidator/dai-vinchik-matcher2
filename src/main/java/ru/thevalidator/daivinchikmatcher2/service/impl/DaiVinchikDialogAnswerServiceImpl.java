package ru.thevalidator.daivinchikmatcher2.service.impl;

import ru.thevalidator.daivinchikmatcher2.service.CaseMatcher;
import ru.thevalidator.daivinchikmatcher2.service.CaseType;
import ru.thevalidator.daivinchikmatcher2.service.DaiVinchikDialogAnswerService;
import ru.thevalidator.daivinchikmatcher2.vk.dto.DaiVinchikDialogAnswer;
import ru.thevalidator.daivinchikmatcher2.vk.dto.MessageAndKeyboard;

public class DaiVinchikDialogAnswerServiceImpl implements DaiVinchikDialogAnswerService {

    private final CaseMatcher matcher;

    public DaiVinchikDialogAnswerServiceImpl() {
        matcher = new CaseMatcherImpl();
    }

    public DaiVinchikDialogAnswerServiceImpl(CaseMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public DaiVinchikDialogAnswer findAnswer(MessageAndKeyboard message) {
        CaseType type = matcher.detectCase(message);
        if (type.equals(CaseType.SKIP_BUTTON)) {

        }

        return null;
    }

}
