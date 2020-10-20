package controller;

import java.text.Normalizer;

public class Utilities {
    private Utilities() {
    }

    public static boolean compareStrings(String key, String value) {
        if (key.length() == 0)
            return true;
        key = Normalizer.normalize(key.toLowerCase().replace(" ", ""), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]",
                "");
        value = Normalizer.normalize(value.toLowerCase().replace(" ", ""), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        return value.contains(key);
    }
}
