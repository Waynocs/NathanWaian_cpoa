package dao.mySQL;

import java.sql.SQLException;

import dao.OrderLineDAO;
import model.OrderLine;

public class MySQLOrderLineDAO implements OrderLineDAO {

    // Instance of the class
    private static MySQLOrderLineDAO instance;

    // constructor
    private MySQLOrderLineDAO() {
    }

    // Insure to get only 1 instance
    public static MySQLOrderLineDAO getInstance() {
        if (instance == null)
            instance = new MySQLOrderLineDAO();

        return instance;
    }

    @Override
    public boolean create(OrderLine object) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean update(OrderLine object) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(OrderLine object) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public OrderLine getById(int order, int product) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OrderLine[] getAllFromOrder(int order) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OrderLine[] getAllFromProduct(int product) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
}
