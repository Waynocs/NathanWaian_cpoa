package dao.mySQL;

import dao.CategoryDAO;

import model.Category;
import Request.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
        Category category = null;

        Connection theConnection = Connexion.creeConnexion();
        PreparedStatement request = theConnection.prepareStatement("select * from 'category'");

        ResultSet reslt = request.executeQuery();

        if (reslt.next()) {
            category = new Category(reslt.getString(1), reslt.getString(2), reslt.getInt(3));
        }

        if (theConnection != null) {
            theConnection.close();
        }

        return category;
    }

    public Category[] getAll() throws SQLException {
        ArrayList<Category> listeCategories = new ArrayList<Category>();

        Connection laConnexion = Connexion.creeConnexion();
        PreparedStatement request = laConnexion.prepareStatement("select * from `Category`");

        ResultSet reslt = request.executeQuery();

        while (reslt.next()) {
            listeCategories.add(new Category(reslt.getString(1), reslt.getString(2), reslt.getInt(3)));
        }

        if (laConnexion != null) {
            laConnexion.close();
        }

        return listeCategories.toArray(new Category[0]);
    }

}
