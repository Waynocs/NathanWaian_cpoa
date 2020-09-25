package dao.mySQL;

import dao.CategoryDAO;
import dao.CustomerDAO;
import dao.DAOFactory;
import dao.OrderDAO;
import dao.OrderLineDAO;

public class MySQLDAOFactory extends DAOFactory {

    @Override
    public CategoryDAO getCategoryDAO() {
        return MySQLCategoryDAO.getInstance();
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

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

}
