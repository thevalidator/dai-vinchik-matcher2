package ru.thevalidator.daivinchikmatcher2.service.daivinchik;

import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.DaiVinchikUserProfile;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.ProfileInitResponse;

public interface DaiVinchikProfileService {

    public ProfileInitResponse fillRandomProfile(DaiVinchikMessageService messageService,
                                                 DaiVinchikDialogAnswerService answerService);
    public ProfileInitResponse fillProfile(DaiVinchikUserProfile profile,
                                           DaiVinchikMessageService messageService,
                                           DaiVinchikDialogAnswerService answerService);

    public DaiVinchikProfileGenerator getProfileGenerator();

}
