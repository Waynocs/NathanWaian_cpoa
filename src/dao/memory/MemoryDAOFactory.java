package dao.memory;

import java.time.LocalDateTime;

import dao.CategoryDAO;
import dao.CustomerDAO;
import dao.OrderDAO;
import dao.OrderLineDAO;
import dao.ProductDAO;
import model.Category;
import model.Customer;
import model.Order;
import model.OrderLine;
import model.Product;

public class MemoryDAOFactory extends dao.DAOFactory {

    private static MemoryDAOFactory instance;

    /**
     * Returns the only instance of the factory
     * 
     * @return the only instance of the factory
     */
    public static MemoryDAOFactory getInstance() {
        return instance == null ? instance = new MemoryDAOFactory() : instance;
    }

    private MemoryDAOFactory() {
        var categ = getCategoryDAO();
        var categ1 = categ.create(new Category("Pulls", "lespulls.png", 1));
        var categ2 = categ.create(new Category("Bonnets", "lesbonnets.png", 2));
        categ.create(new Category("Chaussettes", "leschaussettes.png", 3));
        var customer = getCustomerDAO();
        var cust = customer.create(new Customer(1, "Pierre", "LAROCHE", "pl@ul.fr", "toto", "12", "rue des étudiants",
                "57990", "Metz", "France"));
        var order = getOrderDAO();
        var ord1 = order.create(new Order(1, LocalDateTime.of(2020, 9, 2, 13, 12), cust.getId()));
        var ord2 = order.create(new Order(2, LocalDateTime.of(2020, 8, 30, 11, 22), cust.getId()));
        var product = getProductDAO();
        var prod1 = product.create(
                new Product(2, "Sonic te kiffe", 41.5, "Inspiré par la saga Sega", categ1.getId(), "pull1.png"));
        var prod2 = product.create(
                new Product(6, "La chaleur des rennes", 15, "Classique mais efficace", categ2.getId(), "bonnet0.png"));
        var prod3 = product.create(new Product(12, "Dall", 35, "Joyeux Noël", categ2.getId(), "bonnet1.png"));
        var orderLine = getOrderLineDAO();
        orderLine.create(new OrderLine(ord1.getId(), prod1.getId(), 41.5, 2));
        orderLine.create(new OrderLine(ord1.getId(), prod2.getId(), 15, 1));
        orderLine.create(new OrderLine(ord2.getId(), prod3.getId(), 35, 4));
    }

    @Override
    public CategoryDAO getCategoryDAO() {
        return MemoryCategoryDAO.getInstance();
    }

    @Override
    public CustomerDAO getCustomerDAO() {
        return MemoryCustomerDAO.getInstance();
    }

    @Override
    public OrderDAO getOrderDAO() {
        return MemoryOrderDAO.getInstance();
    }

    @Override
    public OrderLineDAO getOrderLineDAO() {
        return MemoryOrderLineDAO.getInstance();
    }

    @Override
    public ProductDAO getProductDAO() {
        return MemoryProductDAO.getInstance();
    }

}
