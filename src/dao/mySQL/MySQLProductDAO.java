package dao.mySQL;

import java.sql.SQLException;

import dao.ProductDAO;
import model.Product;

public class MySQLProductDAO implements ProductDAO {

    // Instance of the class
    private static MySQLProductDAO instance;

    // constructor
    private MySQLProductDAO() {
    }

    // Insure to get only 1 instance
    public static MySQLProductDAO getInstance() {
        if (instance == null)
            instance = new MySQLProductDAO();

        return instance;
    }

    @Override
    public boolean create(Product object) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean update(Product object) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(Product object) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Product getById(int id) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Product[] getAll() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
}
