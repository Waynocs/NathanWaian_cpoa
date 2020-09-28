package dao.mySQL;

import dao.CategoryDAO;

import model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

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
    public boolean create(Category object) {
        try {
            var statement = Request.Connection.getConnection()
                    .prepareStatement("INSERT INTO `categorie`(`id_categorie`, `titre`, `visuel`) VALUES ("
                            + object.getId() + ", " + object.getName() + ", " + object.getImagePath());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean update(Category object) {
        try {
            var statement = Request.Connection.getConnection()
                    .prepareStatement("UPDATE `categorie` SET `id_categorie`= " + object.getId() + ",`titre`= "
                            + object.getName() + ",`visuel`= " + object.getImagePath());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean delete(Category object) {
        try {
            var statement = Request.Connection.getConnection()
                    .prepareStatement("DELETE FROM `categorie` WHERE `id_categorie` = " + object.getId());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // method pr recuperer depuis la bdd un item
    public Category getById(int id) {
        try {
            var statement = Request.Connection.getConnection().createStatement();
            var result = statement.executeQuery("SELECT `id_categorie`, `titre`, `visuel`=" + id);
            return result.next()
                    ? new Category(result.getString("title"), result.getString("visuel"), result.getInt("id_categorie"))
                    : null;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * show all category
     * 
     * @param id
     * @param title
     * @param visuel
     */

    public Category[] getAll() {
        try {
            final var statement = Request.Connection.getConnection().createStatement();
            final ResultSet result = statement
                    .executeQuery("SELECT `id_categorie`, `titre`, `visuel` FROM `categorie`");
            var categoryList = new LinkedList<Category>();
            while (result.next())
                categoryList.add(new Category(result.getString("title"), result.getString("visuel"),
                        result.getInt("id_categorie")));
            return categoryList.toArray(new Category[0]);
        } catch (SQLException e) {
            return new Category[0];
        }
    }
}
