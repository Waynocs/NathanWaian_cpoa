package dao.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import dao.ProductDAO;
import model.Product;
import request.Connection;

/**
 * Class used to manage products using the MySQLDAOFactory
 */
public class MySQLProductDAO implements ProductDAO {

    private static MySQLProductDAO instance;

    private MySQLProductDAO() {
    }

    /**
     * Returns the only instance of this class
     * 
     * @return the only instance of this class
     */
    public static MySQLProductDAO getInstance() {
        if (instance == null)
            instance = new MySQLProductDAO();

        return instance;
    }

    @Override
    public boolean create(Product object) {
        try {
            var statement = Connection.getConnection().prepareStatement(
                    "INSERT INTO `produit` (`nom`, `description`, `tarif`, `visuel`, `id_categorie`) VALUES ('"
                            + object.getName() + "', '" + object.getDescription() + "', " + object.getCost() + ", '"
                            + object.getImagePath() + "', " + object.getCategory() + ")");

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Product object) {
        try {
            var statement = Connection.getConnection()
                    .prepareStatement("UPDATE `produit` SET `nom`= '" + object.getName() + "',`description`= '"
                            + object.getDescription() + "',`tarif`= " + object.getCost() + ",`visuel`= '"
                            + object.getImagePath() + "',`id_categorie`= " + object.getCategory()
                            + " WHERE `id_produit` = " + object.getId());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Product object) {
        try {
            var statement = Connection.getConnection()
                    .prepareStatement("DELETE FROM `produit` WHERE `id_produit` = " + object.getId());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Product getById(int id) {
        try {
            var statement = Connection.getConnection().createStatement();
            var result = statement.executeQuery(
                    "SELECT `id_produit`, `nom`, `description`, `tarif`, `visuel`, `id_categorie` FROM `produit` WHERE `id_produit`="
                            + id);
            return result.next()
                    ? new Product(result.getInt("id_produit"), result.getString("nom"), result.getDouble("tarif"),
                            result.getString("description"), result.getInt("id_categorie"), result.getString("visuel"))
                    : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product[] getAll() {
        try {
            final var statement = Connection.getConnection().createStatement();
            final ResultSet result = statement.executeQuery(
                    "SELECT `id_produit`, `nom`, `description`, `tarif`, `visuel`, `id_categorie` FROM `produit`");
            var productList = new LinkedList<Product>();
            while (result.next())
                productList.add(new Product(result.getInt("id_produit"), result.getString("nom"),
                        result.getDouble("tarif"), result.getString("description"), result.getInt("id_categorie"),
                        result.getString("visuel")));
            return productList.size() > 0 ? productList.toArray(new Product[0]) : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Product[0];
        }
    }
}
