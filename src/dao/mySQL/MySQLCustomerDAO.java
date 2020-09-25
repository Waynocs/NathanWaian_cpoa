package dao.mySQL;

import java.sql.SQLException;

import dao.CustomerDAO;
import model.Customer;

public class MySQLCustomerDAO implements CustomerDAO {

    // Instance of the class
    private static MySQLCustomerDAO instance;

    // constructor
    private MySQLCustomerDAO() {
    }

    // Insure to get only 1 instance
    public static MySQLCustomerDAO getInstance() {
        if (instance == null)
            instance = new MySQLCustomerDAO();

        return instance;
    }

    @Override
    public boolean create(Customer object) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean update(Customer object) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(Customer object) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Customer getById(int id) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Customer[] getAll() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
}
