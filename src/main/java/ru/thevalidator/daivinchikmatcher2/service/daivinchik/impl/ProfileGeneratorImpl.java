package ru.thevalidator.daivinchikmatcher2.service.daivinchik.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.ProfileGenerator;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.DaiVinchikUserProfile;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.Gender;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.settings.ProfileGeneratorSettings;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import static ru.thevalidator.daivinchikmatcher2.util.RandomSelectByWeightUtil.getRandomValue;

@Component
public class ProfileGeneratorImpl implements ProfileGenerator {

    private final ProfileGeneratorSettings settings;
    private final Map<String, Integer> cities;
    private final Set<String> maleNames;
    private final Set<String> femaleNames;


    @Autowired
    public ProfileGeneratorImpl(ProfileGeneratorSettings profileGeneratorSettings,
                                @Qualifier("cities") Map<String, Integer> cities,
                                @Qualifier("maleNames") Set<String> maleNames,
                                @Qualifier("femaleNames") Set<String> femaleNames) {
        this.settings = profileGeneratorSettings;
        this.cities = cities;
        this.maleNames = maleNames;
        this.femaleNames = femaleNames;
    }


    @Override
    public DaiVinchikUserProfile generateProfile() {
        Gender userGender = Gender.valueOf(getRandomValue(settings.getUserGender().getMapOfValues()));
        Gender genderToSearch = Gender.valueOf(getRandomValue(settings.getGenderToSearch().getMapOfValues()));
        String city = getRandomValue(cities);
        String name = userGender.equals(Gender.FEMALE) ? getRandomValue(femaleNames) : getRandomValue(maleNames);

        DaiVinchikUserProfile profile = new DaiVinchikUserProfile();
        profile.setAge(19);
        profile.setGender(userGender);
        profile.setGenderToSearch(genderToSearch);
        profile.setCity(city);
        profile.setName(name);
//        profile.setProfileText();
//        profile.setPhoto();

        return profile;
    }

}
