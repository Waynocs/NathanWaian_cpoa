package view;

import java.util.HashMap;
import java.util.Map;

import dao.DAOFactory;
import model.Category;

/**
 * Part of the console interface where the user can change the categories
 */
public class Categories {
    private Categories() {
    }

    /**
     * Open the category options
     * 
     * @param factory factory used
     */
    public static void open(DAOFactory factory) {
        int input;
        do {
            System.out.print(Utilities.getSeparator());
            input = Utilities.getUserSelection(
                    "1. See categories\n2. See details of one category\n3. Add new category\n4. Edit existing category\n5. Delete existing category\n6. Back",
                    6);
            switch (input) {
                case 1:
                    seeAll(factory);
                    break;
                case 2:
                    seeItem(factory);
                    break;
                case 3:
                    newItem(factory);
                    break;
                case 4:
                    editItem(factory);
                    break;
                case 5:
                    removeItem(factory);
                    break;
            }
        } while (input != 6);

    }

    private static void seeAll(DAOFactory factory) {
        Category[] categories = factory.getCategoryDAO().getAll();
        Utilities.displayList(categories, (categ) -> categ.getId() + ":" + categ.getName());
    }

    private static void removeItem(DAOFactory factory) {
        int input = -1;
        System.out.print(Utilities.getSeparator());
        do {
            System.out.println("Enter the id of the category to delete");
            if (Utilities.getConsoleInput().hasNextInt()) {
                input = Utilities.getConsoleInput().nextInt();
                if (input < 0) {
                    System.out.println("Enter a number higher than zero :");
                    input = -1;
                }
            } else
                System.out.println("Enter a number :");
            Utilities.getConsoleInput().nextLine();
        } while (input == -1);
        var categ = factory.getCategoryDAO().getById(input);
        if (categ == null) {
            System.out.println("This category doesn't exist.");
            return;
        }
        factory.getCategoryDAO().delete(categ);
    }

    private static void seeItem(DAOFactory factory) {
        int input = -1;
        System.out.print(Utilities.getSeparator());
        do {
            System.out.println("Enter the id of the category to display");
            if (Utilities.getConsoleInput().hasNextInt()) {
                input = Utilities.getConsoleInput().nextInt();
                if (input < 0) {
                    System.out.println("Enter a number higher than zero :");
                    input = -1;
                }
            } else
                System.out.println("Enter a number :");
            Utilities.getConsoleInput().nextLine();
        } while (input == -1);
        var categ = factory.getCategoryDAO().getById(input);
        if (categ == null) {
            System.out.println("This category doesn't exist.");
            return;
        }
        System.out.println(Utilities.getSeparator());
        System.out.print(Utilities.mapToString(getMap(categ, true)));
        System.out.println("\n>>>Press Enter to go back to menu---");
        Utilities.getConsoleInput().nextLine();
    }

    private static Map<Object, Object> getMap(Category category, boolean useID) {
        var details = new HashMap<Object, Object>();
        if (useID)
            details.put("ID", category.getId());
        details.put("Name", category.getName());
        details.put("Visual", category.getImagePath());
        return details;
    }

    private static void editItem(DAOFactory factory) {

        int input = -1;
        System.out.print(Utilities.getSeparator());
        do {
            System.out.println("Enter the id of the category to edit");
            if (Utilities.getConsoleInput().hasNextInt()) {
                input = Utilities.getConsoleInput().nextInt();
                if (input < 0) {
                    System.out.println("Enter a number higher than zero :");
                    input = -1;
                }
            } else
                System.out.println("Enter a number :");
            Utilities.getConsoleInput().nextLine();
        } while (input == -1);
        var categ = factory.getCategoryDAO().getById(input);
        if (categ == null) {
            System.out.println("This category doesn't exist.");
            return;
        }
        do {
            System.out.println(Utilities.getSeparator());
            input = Utilities.getUserSelection(Utilities.mapToString(getMap(categ, false))
                    + "────────────\n1. Edit name\n2. Edit image file\n3. Cancel\n4. Finish", 4);
            if (input == 1) {
                System.out.print("Enter the new name :\n>");
                categ.setName(Utilities.getConsoleInput().nextLine());
            } else if (input == 2) {
                System.out.print("Enter the new image file :\n>");
                categ.setImagePath(Utilities.getConsoleInput().nextLine());
            }
        } while (input < 3);
        if (input == 4)
            if (!factory.getCategoryDAO().update(categ))
                System.out.println("[ERROR] Unable to edit the item");
    }

    private static void newItem(DAOFactory factory) {
        var tmpItem = new Category("name", "file.jpg", 0);
        int input;
        do {
            System.out.println(Utilities.getSeparator());
            input = Utilities.getUserSelection(Utilities.mapToString(getMap(tmpItem, false))
                    + "────────────\n1. Edit name\n2. Edit image file\n3. Cancel\n4. Add", 4);
            if (input == 1) {
                System.out.print("Enter the new name :\n>");
                tmpItem.setName(Utilities.getConsoleInput().nextLine());
            } else if (input == 2) {
                System.out.print("Enter the new image file :\n>");
                tmpItem.setImagePath(Utilities.getConsoleInput().nextLine());
            }
        } while (input < 3);
        if (input == 4)
            if (factory.getCategoryDAO().create(tmpItem) != null)
                System.out.println("[ERROR] Unable to create the item");
    }
}
