package dao.mySQL;

import dao.CategoryDAO;

import model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Class used to manage categories using the MySQLDAOFactory
 */
public class MySQLCategoryDAO implements CategoryDAO {

    private static MySQLCategoryDAO instance;

    private MySQLCategoryDAO() {
    }

    /**
     * Returns the only instance of this class
     * 
     * @return the only instance of this class
     */
    public static MySQLCategoryDAO getInstance() {
        if (instance == null)
            instance = new MySQLCategoryDAO();

        return instance;
    }

    @Override
    public boolean create(Category object) {
        try {
            var statement = Request.Connection.getConnection()
                    .prepareStatement("INSERT INTO `categorie`(`id_categorie`, `titre`, `visuel`) VALUES ("
                            + object.getId() + ", " + object.getName() + ", " + object.getImagePath());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Category object) {
        try {
            var statement = Request.Connection.getConnection()
                    .prepareStatement("UPDATE `categorie` SET `id_categorie`= " + object.getId() + ",`titre`= "
                            + object.getName() + ",`visuel`= " + object.getImagePath());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Category object) {
        try {
            var statement = Request.Connection.getConnection()
                    .prepareStatement("DELETE FROM `categorie` WHERE `id_categorie` = " + object.getId());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Category getById(int id) {
        try {
            var statement = Request.Connection.getConnection().createStatement();
            var result = statement.executeQuery("SELECT `id_categorie`, `titre`, `visuel`=" + id);
            return result.next()
                    ? new Category(result.getString("title"), result.getString("visuel"), result.getInt("id_categorie"))
                    : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
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
            e.printStackTrace();
            return new Category[0];
        }
    }
}
