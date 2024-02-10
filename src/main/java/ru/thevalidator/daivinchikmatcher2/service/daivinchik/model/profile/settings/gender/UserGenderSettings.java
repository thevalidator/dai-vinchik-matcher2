package ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.settings.gender;

import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.Gender;

import java.util.HashMap;
import java.util.Map;

public class UserGenderSettings {
    private int maleWeight;
    private int femaleWeight;

    public UserGenderSettings(int maleWeight, int femaleWeight) {
        this.maleWeight = maleWeight;
        this.femaleWeight = femaleWeight;
    }

    public int getMaleWeight() {
        return maleWeight;
    }

    public void setMaleWeight(int maleWeight) {
        this.maleWeight = maleWeight;
    }

    public int getFemaleWeight() {
        return femaleWeight;
    }

    public void setFemaleWeight(int femaleWeight) {
        this.femaleWeight = femaleWeight;
    }

    public Map<String, Integer> getMapOfValues() {
        Map<String, Integer> values = new HashMap<>();
        values.put(Gender.MALE.name(), maleWeight);
        values.put(Gender.FEMALE.name(), femaleWeight);
        return values;
    }

}
