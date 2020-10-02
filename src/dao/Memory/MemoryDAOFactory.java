package dao.memory;

import dao.CategoryDAO;
import dao.CustomerDAO;
import dao.OrderDAO;
import dao.OrderLineDAO;
import dao.ProductDAO;

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
