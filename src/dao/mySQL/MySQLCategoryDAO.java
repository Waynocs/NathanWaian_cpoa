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
        var statement = Request.Connection.getConnection()
                .prepareStatement("INSERT INTO `categorie`(`id_categorie`, `titre`, `visuel`) VALUES (" + object.getId()
                        + ", " + object.getName() + ", " + object.getImagePath());

        return statement.executeUpdate() != 0;
    }

    public boolean update(Category object) throws SQLException {
        var statement = Request.Connection.getConnection().prepareStatement("UPDATE `categorie` SET `id_categorie`= "
                + object.getId() + ",`titre`= " + object.getName() + ",`visuel`= " + object.getImagePath());

        return statement.executeUpdate() != 0;
    }

    public boolean delete(Category object) throws SQLException {
        var statement = Request.Connection.getConnection()
                .prepareStatement("DELETE FROM `categorie` WHERE `id_categorie` = " + object.getId());

        return statement.executeUpdate() != 0;
    }

    // method pr recuperer depuis la bdd un item
    public Category getById(int id) throws SQLException {
        var statement = Request.Connection.getConnection().createStatement();
        var result = statement.executeQuery("SELECT `id_categorie`, `titre`, `visuel`=" + id);
        return result.next()
                ? new Category(result.getString("title"), result.getString("visuel"), result.getInt("id_categorie"))
                : null;
    }

    public Category[] getAll() throws SQLException {
        return null;

    }
}
