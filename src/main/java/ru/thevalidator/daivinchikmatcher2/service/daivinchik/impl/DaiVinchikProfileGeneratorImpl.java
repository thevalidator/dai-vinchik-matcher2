package ru.thevalidator.daivinchikmatcher2.service.daivinchik.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.ProfileGenerator;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.DaiVinchikUserProfile;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.Gender;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.settings.ProfileGeneratorSettings;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static ru.thevalidator.daivinchikmatcher2.util.RandomSelectByWeightUtil.getRandomValue;

@Component
public class ProfileGeneratorImpl implements ProfileGenerator {

    public static final Logger LOG = LoggerFactory.getLogger(ProfileGeneratorImpl.class);

    private final ProfileGeneratorSettings settings;
    private final Map<String, Integer> cities;
    private final Set<String> maleNames;
    private final Set<String> femaleNames;
    private final List<String> maleText;
    private final List<String> femaleText;
    private final List<String> malePics;
    private final List<String> femalePics;


    @Autowired
    public ProfileGeneratorImpl(ProfileGeneratorSettings profileGeneratorSettings,
                                @Qualifier("cities") Map<String, Integer> cities,
                                @Qualifier("maleNames") Set<String> maleNames,
                                @Qualifier("femaleNames") Set<String> femaleNames,
                                @Qualifier("maleText") List<String> maleText,
                                @Qualifier("femaleText") List<String> femaleText,
                                @Qualifier("malePics") List<String> malePics,
                                @Qualifier("femalePics") List<String> femalePics) {
        this.settings = profileGeneratorSettings;
        this.cities = cities;
        this.maleNames = maleNames;
        this.femaleNames = femaleNames;
        this.maleText = maleText;
        this.femaleText = femaleText;
        this.malePics = malePics;
        this.femalePics = femalePics;
    }


    @Override
    public DaiVinchikUserProfile generateProfile() {
        Gender userGender = Gender.valueOf(getRandomValue(settings.getUserGender().getMapOfValues()));
        Gender genderToSearch = Gender.valueOf(getRandomValue(settings.getGenderToSearch().getMapOfValues()));
        int age = new Random().nextInt(settings.getMaxAge() - settings.getMinAge() + 1) + settings.getMinAge();
        String city = getRandomValue(cities);
        String name = userGender.equals(Gender.FEMALE) ? getRandomValue(femaleNames) : getRandomValue(maleNames);
        String text = userGender.equals(Gender.FEMALE) ? getRandomValue(femaleText) : getRandomValue(maleText);
        String avatar = userGender.equals(Gender.FEMALE) ? getRandomValue(femalePics) : getRandomValue(malePics);

        DaiVinchikUserProfile profile = new DaiVinchikUserProfile();
        profile.setAge(age);
        profile.setGender(userGender);
        profile.setGenderToSearch(genderToSearch);
        profile.setCity(city);
        profile.setName(name);
        profile.setProfileText(text);
        profile.setPhoto(avatar);

        LOG.debug("Profile generated: {}", profile);

        return profile;
    }

}
