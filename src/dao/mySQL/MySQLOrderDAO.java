package dao.mySQL;

import java.sql.SQLException;

import dao.OrderDAO;
import model.Order;

public class MySQLOrderDAO implements OrderDAO {

    // Instance of the class
    private static MySQLOrderDAO instance;

    // constructor
    private MySQLOrderDAO() {
    }

    // Insure to get only 1 instance
    public static MySQLOrderDAO getInstance() {
        if (instance == null)
            instance = new MySQLOrderDAO();

        return instance;
    }

    @Override
    public boolean create(Order object) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean update(Order object) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(Order object) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Order getById(int id) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Order[] getAll() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
}
