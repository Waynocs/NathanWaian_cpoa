package controller;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.function.Predicate;

import javafx.util.StringConverter;

public class Utilities {
    private Utilities() {
    }

    public static String normalize(String str) {
        return Normalizer.normalize(str.toLowerCase().replaceAll("[ \t\n]", ""), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

    public static boolean compareStrings(String key, String value) {
        key = normalize(key);
        if (key.length() == 0)
            return true;
        value = normalize(value);
        int valueIndex = 0;
        for (int i = 0; i < key.length(); valueIndex++) {
            if (valueIndex == value.length())
                return false;
            if (value.charAt(valueIndex) == key.charAt(i))
                ++i;
        }
        return true;
    }

    public static <T> boolean testAny(Iterable<? extends T> array, Predicate<? super T> test, boolean ifEmpty) {
        int iterations = 0;
        for (T t : array) {
            if (test.test(t))
                return true;
            ++iterations;
        }
        return iterations > 0 ? false : ifEmpty;
    }

    public static <T> boolean testAll(Iterable<? extends T> array, Predicate<? super T> test, boolean ifEmpty) {
        int iterations = 0;
        for (T t : array) {
            if (!test.test(t))
                return false;
            ++iterations;
        }
        return iterations > 0 ? true : ifEmpty;
    }

    public static <T> boolean testAny(T[] array, Predicate<? super T> test, boolean ifEmpty) {
        int iterations = 0;
        for (T t : array) {
            if (test.test(t))
                return true;
            ++iterations;
        }
        return iterations > 0 ? false : ifEmpty;
    }

    public static <T> boolean testAll(T[] array, Predicate<? super T> test, boolean ifEmpty) {
        int iterations = 0;
        for (T t : array) {
            if (!test.test(t))
                return false;
            ++iterations;
        }
        return iterations > 0 ? true : ifEmpty;
    }

    public static boolean validDateFormat(String date, StringConverter<LocalDate> formatter) {
        try {
            formatter.fromString(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static String getHiddenString(String original) {
        var res = "";
        for (int i = 0; i < original.length(); ++i)
            res += '•';
        return res;
    }
}
