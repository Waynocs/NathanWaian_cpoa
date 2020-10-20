package controller;

import java.text.Normalizer;

public class Utilities {
    private Utilities() {
    }

    public static boolean compareStrings(String key, String value) {
        if (key.length() == 0)
            return true;
        key = Normalizer.normalize(key.toLowerCase().replace(" ", "").replaceAll("[^\\p{ASCII}]", ""),
                Normalizer.Form.NFD);
        value = Normalizer.normalize(value.toLowerCase().replace(" ", "").replaceAll("[^\\p{ASCII}]", ""),
                Normalizer.Form.NFD);
        return value.contains(key);
    }
}
