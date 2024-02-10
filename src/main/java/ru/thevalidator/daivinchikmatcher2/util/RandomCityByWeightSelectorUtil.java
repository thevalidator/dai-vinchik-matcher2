package ru.thevalidator.daivinchikmatcher2.util;

import java.security.SecureRandom;
import java.util.Map;

public class RandomCityByWeightSelectorUtil {

    private static final SecureRandom random = new SecureRandom();

    public static String getCityName(final Map<String, Integer> cities) {
        String randomCity = null;
        final int totalWeight = cities.values().stream().reduce(0, Integer::sum);
        double randomWeight = random.nextDouble() * totalWeight;
        for (Map.Entry<String, Integer> e: cities.entrySet()) {
            if (randomWeight < e.getValue()) {
                randomCity = e.getKey();
                break;
            }
            randomWeight -= e.getValue();
        }
        return randomCity;
    }

}
