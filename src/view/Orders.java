package view;

import java.util.HashMap;
import java.util.Map;

import dao.DAOFactory;
import model.Order;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Part of the console interface where the user can change the orders
 */
public class Orders {
    private Orders() {
    }

    /**
     * Open the order options
     * 
     * @param factory factory used
     */
    public static void open(DAOFactory factory) {
        int input;
        do {
            System.out.print(Utilities.getSeparator());
            input = Utilities.getUserSelection(
                    "1. See orders\n2. See details of one order\n3. Add new order\n4. Edit existing order\n5. Delete existing order\n6. Back",
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
        Order[] orders = factory.getOrderDAO().getAll();
        Utilities.displayList(orders, (ord) -> ord.getId() + " | customer : " + ord.getCustomer() + ", "
                + ord.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private static void removeItem(DAOFactory factory) {
        int input = -1;
        System.out.print(Utilities.getSeparator());
        do {
            System.out.println("Enter the id of the order to delete");
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
        var ord = factory.getOrderDAO().getById(input);
        if (ord == null) {
            System.out.println("This order doesn't exist.");
            return;
        }
        factory.getOrderDAO().delete(ord);
    }

    private static void seeItem(DAOFactory factory) {
        int input = -1;
        System.out.print(Utilities.getSeparator());
        do {
            System.out.println("Enter the id of the order to display");
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
        var ord = factory.getOrderDAO().getById(input);
        if (ord == null) {
            System.out.println("This order doesn't exist.");
            return;
        }
        System.out.println(Utilities.getSeparator());
        System.out.print(Utilities.mapToString(getMap(ord, true)));
        System.out.println("\n>>>Press Enter to go back to menu---");
        Utilities.getConsoleInput().nextLine();
    }

    private static Map<Object, Object> getMap(Order order, boolean useID) {
        var details = new HashMap<Object, Object>();
        if (useID)
            details.put("ID", order.getId());
        details.put("Customer id", order.getCustomer());
        details.put("Date", order.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return details;
    }

    private static void editItem(DAOFactory factory) {

        int input = -1;
        System.out.print(Utilities.getSeparator());
        do {
            System.out.println("Enter the id of the order to edit");
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
        var ord = factory.getOrderDAO().getById(input);
        if (ord == null) {
            System.out.println("This order doesn't exist.");
            return;
        }
        do {
            System.out.println(Utilities.getSeparator());
            input = Utilities.getUserSelection(Utilities.mapToString(getMap(ord, false))
                    + "────────────\n1. Edit customer ID\n2. Edit date\n3. Cancel\n4. Finish", 4);
            if (input == 1) {
                Utilities.displayList(factory.getCustomerDAO().getAll(),
                        (c) -> c.getId() + ":" + c.getName() + " " + c.getSurname());
                int input2 = -1;
                do {
                    System.out.println("Enter the id of the customer to chose");
                    if (Utilities.getConsoleInput().hasNextInt()) {
                        input2 = Utilities.getConsoleInput().nextInt();
                        if (input2 < 0) {
                            System.out.println("Enter a number higher than zero :");
                            input2 = -1;
                        }
                    } else
                        System.out.println("Enter a number :");
                    Utilities.getConsoleInput().nextLine();
                } while (input2 == -1);
                ord.setCustomer(input2);
            } else if (input == 2) {
                System.out.print("Enter the new date (yyyy-MM-dd HH:mm) :\n>");
                var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                try {
                    var dateTime = LocalDateTime.parse(Utilities.getConsoleInput().nextLine(), formatter);
                    ord.setDate(dateTime);
                } catch (DateTimeParseException e) {
                    System.out.println("[ERROR] " + e.getMessage());
                }
            }
        } while (input < 3);
        if (input == 4)
            if (!factory.getOrderDAO().update(ord))
                System.out.println("[ERROR] Unable to edit the item");
    }

    private static void newItem(DAOFactory factory) {
        var tmpItem = new Order(0, LocalDateTime.now(), -1);
        int input;
        do {
            System.out.println(Utilities.getSeparator());
            input = Utilities.getUserSelection(Utilities.mapToString(getMap(tmpItem, false))
                    + "────────────\n1. Edit customer ID\n2. Edit date\n3. Cancel\n4. Finish", 4);
            if (input == 1) {
                Utilities.displayList(factory.getCustomerDAO().getAll(),
                        (c) -> c.getId() + ":" + c.getName() + " " + c.getSurname());
                int input2 = -1;
                do {
                    System.out.println("Enter the id of the customer to chose");
                    if (Utilities.getConsoleInput().hasNextInt()) {
                        input2 = Utilities.getConsoleInput().nextInt();
                        if (input2 < 0) {
                            System.out.println("Enter a number higher than zero :");
                            input2 = -1;
                        }
                    } else
                        System.out.println("Enter a number :");
                    Utilities.getConsoleInput().nextLine();
                } while (input2 == -1);
                tmpItem.setCustomer(input2);
            } else if (input == 2) {
                System.out.print("Enter the new date (yyyy-MM-dd HH:mm) :\n>");
                var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                try {
                    var dateTime = LocalDateTime.parse(Utilities.getConsoleInput().nextLine(), formatter);
                    tmpItem.setDate(dateTime);
                } catch (DateTimeParseException e) {
                    System.out.println("[ERROR] " + e.getMessage());
                }
            }
        } while (input < 3);
        if (input == 4)
            if (!factory.getOrderDAO().create(tmpItem))
                System.out.println("[ERROR] Unable to create the item");
    }
}
