package dao.mySQL;

import dao.CategoryDAO;

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

    // method pr recuperer depuis la bdd un item
    public Category getById(int id) throws SQLException {

        return null;
    }

    public Category[] getAll() throws SQLException {
        return null;

    }
}
