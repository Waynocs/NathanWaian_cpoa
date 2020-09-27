package view;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Function;

public class Utilities {
    private Utilities() {
    }

    private static Scanner consoleInput;

    /**
     * Returns a long horizontal line after several new lines
     * 
     * @return a String used as a separator
     */
    public static String getSeparator() {
        return "\n\n\n\n\n\n─────────────────────────────────────────────\n";
    }

    /**
     * Returns the one and only scanner that should be used to read the console
     * 
     * @return the one and only scanner that should be used to read the console
     */
    public static Scanner getConsoleInput() {
        return consoleInput == null ? consoleInput = new Scanner(System.in) : consoleInput;
    }

    /**
     * Returns the number typed by the user, with the minimal value set to 1
     * 
     * @param message  message to be displayed on the screen
     * @param maxValue maximal value (inclusive)
     * @return the number typed by the user
     */
    public static int getUserSelection(String message, int maxValue) {
        return getUserSelection(message, 1, maxValue);
    }

    /**
     * Returns the number typed by the user
     * 
     * @param message  message to be displayed on the screen
     * @param minValue minimal value (inclusive)
     * @param maxValue maximal value (inclusive)
     * @return the number typed by the user
     */
    public static int getUserSelection(String message, int minValue, int maxValue) {
        int input = minValue - 1;
        var console = getConsoleInput();
        do {
            System.out.print(message + "\n>");
            if (console.hasNextInt()) {
                input = console.nextInt();
                if (input < minValue || maxValue < input)
                    System.out.println("[ERROR] You must type a valid number");
            } else
                System.out.println("[ERROR] You must type a number");
            console.nextLine(); // free the buffer
        } while (minValue > input || input > maxValue);
        return input;
    }

    /**
     * Parses a number from either the french locale of the english locale
     * 
     * @param value string to parse into a number
     * @return number parsed from the value
     */
    public static Number parseNumber(String value) {
        var frenchFormat = NumberFormat.getInstance(Locale.FRENCH);
        var englishFormat = NumberFormat.getInstance(Locale.ENGLISH);
        try {
            return englishFormat.parse(value);
        } catch (ParseException e1) {
            try {
                return frenchFormat.parse(value);
            } catch (ParseException e2) {
                return null;
            }
        }
    }

    /**
     * Displays the given list in the console
     * 
     * @param <T>          type of the objects in the list
     * @param list         list to display
     * @param entryPerPage the amount of objects displayed per page
     * @param formatter    the formatter used to convert the input type to another
     *                     object which will use the toString() method
     */
    public static <T> void displayList(T[] list, int entryPerPage, Function<T, Object> formatter) {
        int input;
        int page = 0;
        int pageCount = (int) Math.ceil(list.length / (double) entryPerPage);
        if (pageCount == 0) {
            System.out.print(Utilities.getSeparator());
            input = Utilities.getUserSelection("No items\n1. Back", 1);
            return;
        }
        do {
            System.out.print(Utilities.getSeparator());
            String message = "Page " + (page + 1) + " out of " + pageCount + '\n';
            for (int i = page * 9; i < Math.min((page + 1) * 9, list.length); i++)
                message += formatter.apply(list[i]) + "\n";
            if (page == 0 && pageCount == 1) {
                message += "1. Back";
                input = Utilities.getUserSelection(message, 1);
                input = 3;
            } else if (page == 0) {
                message += "1. --> Next\n2. Back";
                input = Utilities.getUserSelection(message, 2);
                if (input == 1)
                    page++;
                else
                    input = 3;
            } else if (page + 1 == pageCount) {
                message += "1. <-- Previous\n2. Back";
                input = Utilities.getUserSelection(message, 2);
                if (input == 1)
                    page--;
                else
                    input = 3;
            } else {
                message += "1. <-- Previous\n2. --> Next\n3. Back";
                input = Utilities.getUserSelection(message, 3);
                if (input == 1)
                    page--;
                else if (input == 2)
                    page++;
            }
        } while (input != 3);
    }

    /**
     * Displays the given list in the console, using 9 objects per page and the
     * toString() method
     * 
     * @param list list to display
     */
    public static void displayList(Object[] list) {
        displayList(list, 9, (obj) -> obj.toString());
    }

    /**
     * Displays the given list in the console, using 9 objects per page
     * 
     * @param <T>       type of the objects in the list
     * @param list      list to display
     * @param formatter the formatter used to convert the input type to another
     *                  object which will use the toString() method
     */
    public static <T> void displayList(T[] list, Function<T, Object> formatter) {
        displayList(list, 9, formatter);
    }

    /**
     * Displays the given list in the console, using the toString() method
     * 
     * @param list         list to display
     * @param entryPerPage the amount of objects displayed per page
     */
    public static void displayList(Object[] list, int entryPerPage) {
        displayList(list, entryPerPage, (obj) -> obj.toString());
    }
}
