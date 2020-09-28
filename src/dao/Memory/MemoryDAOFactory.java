package dao.Memory;

import dao.CategoryDAO;
import dao.CustomerDAO;
import dao.OrderDAO;
import dao.OrderLineDAO;

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OrderDAO getOrderDAO() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OrderLineDAO getOrderLineDAO() {
        // TODO Auto-generated method stub
        return null;
    }

}
