package dao.mySQL;

import dao.CategoryDAO;
import dao.CustomerDAO;
import dao.DAOFactory;
import dao.OrderDAO;
import dao.OrderLineDAO;
import dao.ProductDAO;

/**
 * SQL factory, to save on an sql server
 */
public class MySQLDAOFactory extends DAOFactory {

    private static MySQLDAOFactory Instance;

    /**
     * Returns the only instance of the factory
     * 
     * @return the only instance of the factory
     */
    public static MySQLDAOFactory getInstance() {
        return Instance == null ? Instance = new MySQLDAOFactory() : Instance;
    }

    private MySQLDAOFactory() {

    }

    @Override
    public CategoryDAO getCategoryDAO() {
        return MySQLCategoryDAO.getInstance();
    }

    @Override
    public CustomerDAO getCustomerDAO() {
        return MySQLCustomerDAO.getInstance();
    }

    @Override
    public OrderDAO getOrderDAO() {
        return MySQLOrderDAO.getInstance();
    }

    @Override
    public OrderLineDAO getOrderLineDAO() {
        return MySQLOrderLineDAO.getInstance();
    }

    @Override
    public ProductDAO getProductDAO() {
        return MySQLProductDAO.getInstance();
    }
}
