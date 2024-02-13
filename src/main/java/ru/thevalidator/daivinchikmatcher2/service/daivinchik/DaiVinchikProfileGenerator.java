package ru.thevalidator.daivinchikmatcher2.service.daivinchik;

import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.DaiVinchikUserProfile;

public interface DaiVinchikProfileGenerator {

    public DaiVinchikUserProfile generateRandomProfile();
    public DaiVinchikUserProfile generateRandomFemaleProfile();
    public DaiVinchikUserProfile generateRandomMaleProfile();
    public DaiVinchikUserProfile generateCustomProfile();

}