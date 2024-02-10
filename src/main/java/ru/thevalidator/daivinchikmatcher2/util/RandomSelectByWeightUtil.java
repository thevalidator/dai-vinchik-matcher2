package ru.thevalidator.daivinchikmatcher2.util;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RandomSelectByWeightUtil {

    private static final SecureRandom random = new SecureRandom();

    public static String getRandomValue(final Map<String, Integer> values) {
        String randomValue = null;
        final int totalWeight = values.values().stream().reduce(0, Integer::sum);
        double randomWeight = random.nextDouble() * totalWeight;
        for (Map.Entry<String, Integer> e: values.entrySet()) {
            randomValue = e.getKey();
            if (randomWeight < e.getValue()) {
                break;
            }
            randomWeight -= e.getValue();
        }
        return randomValue;
    }

    public static String getRandomValue(final Set<String> values) {
        String value = null;
        int index = random.nextInt(values.size());
        for (String v: values) {
            if (index-- == 0) {
                value = v;
            }
        }
        return value;
    }

    public static String getRandomValue(final List<String> values) {
        int index = random.nextInt(values.size());
        return values.get(index);
    }

}
