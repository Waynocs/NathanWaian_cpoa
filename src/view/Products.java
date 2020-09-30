package view;

import java.util.HashMap;
import java.util.Map;

import dao.DAOFactory;
import model.Product;

/**
 * Part of the console interface where the user can change the orders
 */
public class Products {
    private Products() {
    }

    /**
     * Open the product options
     * 
     * @param factory factory used
     */
    public static void open(DAOFactory factory) {
        int input;
        do {
            System.out.print(Utilities.getSeparator());
            input = Utilities.getUserSelection(
                    "1. See products\n2. See details of one product\n3. Add new product\n4. Edit existing product\n5. Delete existing product\n6. Back",
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
        var products = factory.getProductDAO().getAll();
        Utilities.displayList(products, (prod) -> prod.getId() + ":" + prod.getName());
    }

    private static void removeItem(DAOFactory factory) {
        int input = -1;
        System.out.print(Utilities.getSeparator());
        do {
            System.out.println("Enter the id of the product to delete");
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
        var prod = factory.getProductDAO().getById(input);
        if (prod == null) {
            System.out.println("This product doesn't exist.");
            return;
        }
        factory.getProductDAO().delete(prod);
    }

    private static void seeItem(DAOFactory factory) {
        int input = -1;
        System.out.print(Utilities.getSeparator());
        do {
            System.out.println("Enter the id of the product to display");
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
        var custo = factory.getProductDAO().getById(input);
        if (custo == null) {
            System.out.println("This product doesn't exist.");
            return;
        }
        System.out.println(Utilities.getSeparator());
        System.out.print(Utilities.mapToString(getMap(custo, true)));
        System.out.println("\n>>>Press Enter to go back to menu---");
        Utilities.getConsoleInput().nextLine();
    }

    private static Map<Object, Object> getMap(Product product, boolean useID) {
        var details = new HashMap<Object, Object>();
        if (useID)
            details.put("ID", product.getId());
        details.put("Name", product.getName());
        details.put("Description", product.getDescription());
        details.put("Cost", product.getCost());
        details.put("ImagePath", product.getImagePath());
        details.put("Category", product.getCategory());
        return details;
    }

    private static void editItem(DAOFactory factory) {

        int input = -1;
        System.out.print(Utilities.getSeparator());
        do {
            System.out.println("Enter the id of the product to edit");
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
        var prod = factory.getProductDAO().getById(input);
        if (prod == null) {
            System.out.println("This product doesn't exist.");
            return;
        }
        do {
            System.out.println(Utilities.getSeparator());
            input = Utilities.getUserSelection(Utilities.mapToString(getMap(prod, false))
                    + "────────────\n1. Edit name\n2. Edit description\n3. Edit cost\n4. Edit imagePath\n5. Edit Category\n6. Cancel\n7. Finish",
                    11);
            if (input == 1) {
                System.out.print("Enter the new name :\n>");
                prod.setName(Utilities.getConsoleInput().nextLine());
            } else if (input == 2) {
                System.out.print("Enter the new description :\n>");
                prod.setDescription(Utilities.getConsoleInput().nextLine());
            } else if (input == 3) {
                System.out.print("Enter the new cost :\n>");
                prod.setCost(Utilities.getConsoleInput().nextDouble());
            } else if (input == 4) {
                System.out.print("Enter the new imagePath :\n>");
                prod.setImagePath(Utilities.getConsoleInput().nextLine());
            } else if (input == 5) {
                System.out.print("Enter the new address number :\n>");
                prod.setCategory(Utilities.getConsoleInput().nextInt());
            }
        } while (input < 5);
        if (input == 6)
            if (!factory.getProductDAO().update(prod))
                System.out.println("[ERROR] Unable to edit the item");
    }

    private static void newItem(DAOFactory factory) {
        var tmpItem = new Product(0, "name", 14.5, "description", 3, "imagePath");
        int input;
        do {
            System.out.println(Utilities.getSeparator());
            input = Utilities.getUserSelection(Utilities.mapToString(getMap(tmpItem, false))
                    + "────────────\n1. Edit name\n2. Edit description\n3. Edit cost\n4. Edit imagePath\n5. Edit category\n6. Cancel\n7. Add",
                    11);
            if (input == 1) {
                System.out.print("Enter the new name :\n>");
                tmpItem.setName(Utilities.getConsoleInput().nextLine());
            } else if (input == 2) {
                System.out.print("Enter the new description :\n>");
                tmpItem.setDescription(Utilities.getConsoleInput().nextLine());
            } else if (input == 3) {
                System.out.print("Enter the new cost :\n>");
                tmpItem.setCost(Utilities.getConsoleInput().nextDouble());
            } else if (input == 4) {
                System.out.print("Enter the new imagePath :\n>");
                tmpItem.setImagePath(Utilities.getConsoleInput().nextLine());
            } else if (input == 5) {
                System.out.print("Enter the new category :\n>");
                tmpItem.setCategory(Utilities.getConsoleInput().nextInt());
            }

        } while (input < 5);
        if (input == 6)
            if (!factory.getProductDAO().create(tmpItem))
                System.out.println("[ERROR] Unable to create the item");

    }
}
