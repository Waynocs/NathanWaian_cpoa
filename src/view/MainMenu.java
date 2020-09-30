package view;

import dao.DAOFactory;
import dao.DAOFactory.Mode;

/**
 * Main menu of the console interface
 */
public class MainMenu {

    private MainMenu() {
    }

    /**
     * Starts the console interface, getting to the main menu
     */
    public static void Start() {
        switch (Utilities.getUserSelection("Choose a mode :\n1. MySQL\n2. Saved in memory", 2)) {
            case 1:
                menu(DAOFactory.getFactory(Mode.SQL));
                break;
            case 2:
                menu(DAOFactory.getFactory(Mode.MEMORY));
                break;
        }
    }

    private static void menu(DAOFactory factory) {
        int input;
        do {
            System.out.print(Utilities.getSeparator());
            input = Utilities.getUserSelection(
                    "1. Open categories\n2. Open products\n3. Open customers\n4. Open orders\n5. Exit", 5);
            switch (input) {
                case 1:
                    Categories.open(factory);
                    break;
                case 3:
                    Customers.open(factory);
                    break;
            }
        } while (input != 5);
    }
}
