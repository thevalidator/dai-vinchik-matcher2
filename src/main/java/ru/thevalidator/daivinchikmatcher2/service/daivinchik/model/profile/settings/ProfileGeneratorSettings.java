package ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.settings;

import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.settings.gender.GenderToSearchSettings;
import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.settings.gender.UserGenderSettings;

public class ProfileGeneratorSettings {

    private final int minAge;
    private final int maxAge;
    private UserGenderSettings userGender;
    private GenderToSearchSettings genderToSearch;

    public ProfileGeneratorSettings(int minAge,
                                    int maxAge,
                                    UserGenderSettings userGender,
                                    GenderToSearchSettings genderToSearch) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.userGender = userGender;
        this.genderToSearch = genderToSearch;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public UserGenderSettings getUserGender() {
        return userGender;
    }

    public void setUserGender(UserGenderSettings userGender) {
        this.userGender = userGender;
    }

    public GenderToSearchSettings getGenderToSearch() {
        return genderToSearch;
    }

    public void setGenderToSearch(GenderToSearchSettings genderToSearch) {
        this.genderToSearch = genderToSearch;
    }

}

