package controller;

import java.util.Scanner;

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
}
