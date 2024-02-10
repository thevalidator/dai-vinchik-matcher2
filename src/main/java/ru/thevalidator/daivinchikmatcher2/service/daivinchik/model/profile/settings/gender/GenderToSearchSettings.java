package ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.settings.gender;

import ru.thevalidator.daivinchikmatcher2.service.daivinchik.model.profile.Gender;

import java.util.Map;

public class GenderToSearchSettings extends UserGenderSettings {

    private int everyoneWeight;

    public GenderToSearchSettings(int maleWeight, int femaleWeight, int everyoneWeight) {
        super(maleWeight, femaleWeight);
        this.everyoneWeight = everyoneWeight;
    }

    public int getEveryoneWeight() {
        return everyoneWeight;
    }

    public void setEveryoneWeight(int everyoneWeight) {
        this.everyoneWeight = everyoneWeight;
    }

    @Override
    public Map<String, Integer> getMapOfValues() {
        Map<String, Integer> values = super.getMapOfValues();
        values.put(Gender.ALL.name(), everyoneWeight);
        return values;
    }

}
