package ru.thevalidator.daivinchikmatcher2.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ImageCheckerUtil {

    private static final Set<String> EXTENSIONS = new HashSet<>(Arrays.asList(".jpg", ".png", ".gif"));

    public static boolean hasRequiredExtension(String filename) {
        String name = filename.toLowerCase();
        for (String e: EXTENSIONS) {
            if (name.endsWith(e)) {
                return true;
            }
        }
        return false;
    }

}
