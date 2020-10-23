package dao.mySQL;

import dao.CategoryDAO;
import dao.DAOException;
import model.Category;
import model.Product;
import request.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public Category create(Category object) {
        try {
            var statement = Connection.getConnection()
                    .prepareStatement("INSERT INTO `categorie`(`titre`, `visuel`) VALUES ('" + object.getName() + "', '"
                            + object.getImagePath() + "')", Statement.RETURN_GENERATED_KEYS);
            var result = statement.executeUpdate();
            if (result == 0)
                return null;
            else {
                var keys = statement.getGeneratedKeys();
                keys.next();
                return getById((int) keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public boolean update(Category object) {
        try {
            var statement = Connection.getConnection()
                    .prepareStatement("UPDATE `categorie` SET `titre`= '" + object.getName() + "',`visuel`= '"
                            + object.getImagePath() + "' WHERE `id_categorie` = " + object.getId());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public boolean delete(Category object) {
        try {
            for (Product product : MySQLProductDAO.getInstance().getAll())
                if (product.getCategory() == object.getId())
                    throw new DAOException("The category '" + object.getId() + "' is used by a product");
            var statement = Connection.getConnection()
                    .prepareStatement("DELETE FROM `categorie` WHERE `id_categorie` = " + object.getId());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Category getById(int id) {
        try {
            var statement = Connection.getConnection().createStatement();
            var result = statement.executeQuery(
                    "SELECT `id_categorie`, `titre`, `visuel` FROM `categorie` WHERE `id_categorie`=" + id);
            return result.next()
                    ? new Category(result.getString("titre"), result.getString("visuel"), result.getInt("id_categorie"))
                    : null;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Category[] getAll() {
        try {
            final var statement = Connection.getConnection().createStatement();
            final ResultSet result = statement
                    .executeQuery("SELECT `id_categorie`, `titre`, `visuel` FROM `categorie`");
            var categoryList = new LinkedList<Category>();
            while (result.next())
                categoryList.add(new Category(result.getString("titre"), result.getString("visuel"),
                        result.getInt("id_categorie")));
            return categoryList.toArray(new Category[0]);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }
}
