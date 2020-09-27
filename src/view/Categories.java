package view;

import java.sql.SQLException;

import dao.DAOFactory;
import model.Category;

public class Categories {
    private Categories() {
    }

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
            }
        } while (input != 6);

    }

    private static void seeAll(DAOFactory factory) {
        Category[] categories;
        try {
            categories = factory.getCategoryDAO().getAll();
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return;
        }
        Utilities.displayList(categories, (categ) -> categ.getId() + ":" + categ.getName());
    }
}
