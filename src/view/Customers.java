package view;

import java.util.HashMap;
import java.util.Map;

import dao.DAOFactory;
import model.Customer;

/**
 * Part of the console interface where the user can change the orders
 */
public class Customers {
    private Customers() {
    }

    /**
     * Open the customer options
     * 
     * @param factory factory used
     */
    public static void open(DAOFactory factory) {
        int input;
        do {
            System.out.print(Utilities.getSeparator());
            input = Utilities.getUserSelection(
                    "1. See customers\n2. See details of one customer\n3. Add new customer\n4. Edit existing customer\n5. Delete existing customer\n6. Back",
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
        var customers = factory.getCustomerDAO().getAll();
        Utilities.displayList(customers, (custo) -> custo.getId() + ":" + custo.getName() + " " + custo.getSurname());
    }

    private static void removeItem(DAOFactory factory) {
        int input = -1;
        System.out.print(Utilities.getSeparator());
        do {
            System.out.println("Enter the id of the customer to delete");
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
        var custo = factory.getCustomerDAO().getById(input);
        if (custo == null) {
            System.out.println("This customer doesn't exist.");
            return;
        }
        factory.getCustomerDAO().delete(custo);
    }

    private static void seeItem(DAOFactory factory) {
        int input = -1;
        System.out.print(Utilities.getSeparator());
        do {
            System.out.println("Enter the id of the customer to display");
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
        var custo = factory.getCustomerDAO().getById(input);
        if (custo == null) {
            System.out.println("This customer doesn't exist.");
            return;
        }
        System.out.println(Utilities.getSeparator());
        System.out.print(Utilities.mapToString(getMap(custo, true)));
        System.out.println("\n>>>Press Enter to go back to menu---");
        Utilities.getConsoleInput().nextLine();
    }

    private static Map<Object, Object> getMap(Customer customer, boolean useID) {
        var details = new HashMap<Object, Object>();
        if (useID)
            details.put("ID", customer.getId());
        details.put("Name", customer.getName());
        details.put("Surname", customer.getSurname());
        details.put("Identifier", customer.getIdentifier());
        details.put("Password", customer.getPwd());
        details.put("Address number", customer.getAddressNumber());
        details.put("Address street", customer.getAddressStreet());
        details.put("Postal Code", customer.getAddressPostalCode());
        details.put("City", customer.getAddressCity());
        details.put("Country", customer.getAddressCountry());
        return details;
    }

    private static void editItem(DAOFactory factory) {

        int input = -1;
        System.out.print(Utilities.getSeparator());
        do {
            System.out.println("Enter the id of the customer to edit");
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
        var custo = factory.getCustomerDAO().getById(input);
        if (custo == null) {
            System.out.println("This customer doesn't exist.");
            return;
        }
        do {
            System.out.println(Utilities.getSeparator());
            input = Utilities.getUserSelection(Utilities.mapToString(getMap(custo, false))
                    + "────────────\n1. Edit name\n2. Edit surname\n3. Edit identifier\n4. Edit password\n5. Edit address number\n6. Edit address street\n7. Edit postal code\n8. Edit city\n9. Edit country\n10. Cancel\n11. Finish",
                    11);
            if (input == 1) {
                System.out.print("Enter the new name :\n>");
                custo.setName(Utilities.getConsoleInput().nextLine());
            } else if (input == 2) {
                System.out.print("Enter the new surname :\n>");
                custo.setSurname(Utilities.getConsoleInput().nextLine());
            } else if (input == 3) {
                System.out.print("Enter the new identifier :\n>");
                custo.setIdentifier(Utilities.getConsoleInput().nextLine());
            } else if (input == 4) {
                System.out.print("Enter the new password :\n>");
                custo.setPwd(Utilities.getConsoleInput().nextLine());
            } else if (input == 5) {
                System.out.print("Enter the new address number :\n>");
                custo.setAddressNumber(Utilities.getConsoleInput().nextLine());
            } else if (input == 6) {
                System.out.print("Enter the new address street :\n>");
                custo.setAddressStreet(Utilities.getConsoleInput().nextLine());
            } else if (input == 7) {
                System.out.print("Enter the new postal code :\n>");
                custo.setAddressPostalCode(Utilities.getConsoleInput().nextLine());
            } else if (input == 8) {
                System.out.print("Enter the new city :\n>");
                custo.setAddressCity(Utilities.getConsoleInput().nextLine());
            } else if (input == 9) {
                System.out.print("Enter the new country :\n>");
                custo.setAddressCountry(Utilities.getConsoleInput().nextLine());
            }
        } while (input < 10);
        if (input == 11)
            if (!factory.getCustomerDAO().update(custo))
                System.out.println("[ERROR] Unable to edit the item");
    }

    private static void newItem(DAOFactory factory) {
        var tmpItem = new Customer(0, "name", "surname", "identifier", "12345", "0", "street", "00000", "city",
                "country");
        int input;
        do {
            System.out.println(Utilities.getSeparator());
            input = Utilities.getUserSelection(Utilities.mapToString(getMap(tmpItem, false))
                    + "────────────\n1. Edit name\n2. Edit surname\n3. Edit identifier\n4. Edit password\n5. Edit address number\n6. Edit address street\n7. Edit postal code\n8. Edit city\n9. Edit country\n10. Cancel\n11. Add",
                    11);
            if (input == 1) {
                System.out.print("Enter the new name :\n>");
                tmpItem.setName(Utilities.getConsoleInput().nextLine());
            } else if (input == 2) {
                System.out.print("Enter the new surname :\n>");
                tmpItem.setSurname(Utilities.getConsoleInput().nextLine());
            } else if (input == 3) {
                System.out.print("Enter the new identifier :\n>");
                tmpItem.setIdentifier(Utilities.getConsoleInput().nextLine());
            } else if (input == 4) {
                System.out.print("Enter the new password :\n>");
                tmpItem.setPwd(Utilities.getConsoleInput().nextLine());
            } else if (input == 5) {
                System.out.print("Enter the new address number :\n>");
                tmpItem.setAddressNumber(Utilities.getConsoleInput().nextLine());
            } else if (input == 6) {
                System.out.print("Enter the new address street :\n>");
                tmpItem.setAddressStreet(Utilities.getConsoleInput().nextLine());
            } else if (input == 7) {
                System.out.print("Enter the new postal code :\n>");
                tmpItem.setAddressPostalCode(Utilities.getConsoleInput().nextLine());
            } else if (input == 8) {
                System.out.print("Enter the new city :\n>");
                tmpItem.setAddressCity(Utilities.getConsoleInput().nextLine());
            } else if (input == 9) {
                System.out.print("Enter the new country :\n>");
                tmpItem.setAddressCountry(Utilities.getConsoleInput().nextLine());
            }
        } while (input < 10);
        if (input == 11)
            if (!factory.getCustomerDAO().create(tmpItem))
                System.out.println("[ERROR] Unable to create the item");

    }
}
