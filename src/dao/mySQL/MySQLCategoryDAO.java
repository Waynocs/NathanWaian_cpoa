package dao.mySQL;

import dao.model.CategoryDAO;

import dao.model.*;
import model.Category;
import java.sql.SQLException;

public class MySQLCategoryDAO implements CategoryDAO {

    // Instance of the class
    private static MySQLCategoryDAO instance;

    // constructor
    private MySQLCategoryDAO() {
    }

    // Insure to get only 1 instance
    public static MySQLCategoryDAO getInstance() {
        if (instance == null)
            instance = new MySQLCategoryDAO();

        return instance;
    }

    // executer une requete SQL
    public boolean create(Category object) throws SQLException {

        return false;
    }

    public boolean update(Category object) throws SQLException {
        return false;
    }

    public boolean delete(Category object) throws SQLException {
        return false;
    }

    public Category getById(int id) throws SQLException {
        Category category = null;
        return category;
    }

}
