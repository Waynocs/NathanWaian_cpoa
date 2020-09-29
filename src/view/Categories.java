package view;

import java.sql.SQLException;

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
                case 3:
                    newItem(factory);
                    break;
            }
        } while (input != 6);

    }

    private static void seeAll(DAOFactory factory) {
        Category[] categories = factory.getCategoryDAO().getAll();
        Utilities.displayList(categories, (categ) -> categ.getId() + ":" + categ.getName());
    }

    private static void newItem(DAOFactory factory) {
        var tmpItem = new Category("name", "file.jpg", 0);
        int input;
        do {
            System.out.println(Utilities.getSeparator());
            input = Utilities.getUserSelection("1. Edit name\n2. Edit image file\n3. Cancel\n4.Add", 4);
            if (input == 1) {
                System.out.print("Enter the new name :\n>");
                tmpItem.setName(Utilities.getConsoleInput().nextLine());
            } else if (input == 2) {
                System.out.print("Enter the new image file :\n>");
                tmpItem.setImagePath(Utilities.getConsoleInput().nextLine());
            }
        } while (input < 3);
        if (input == 4)
            factory.getCategoryDAO().create(tmpItem);
    }
}
