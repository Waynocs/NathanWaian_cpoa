package dao.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

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

    /**
     * create a new product
     * 
     * @param id
     * @param name
     * @param description
     * @param cost
     * @param imagePath
     * @param categoryID
     */

    @Override
    public boolean create(Product object) throws SQLException {
        var statement = Request.Connection.getConnection().prepareStatement(
                "INSERT INTO `produit` (`id_produit`, `nom`, `description`, `tarif`, `visuel`, `id_categorie`) VALUES ("
                        + object.getId() + ", " + object.getName() + ", " + object.getDescription() + ", "
                        + object.getCost() + ", " + object.getImagePath() + ", " + object.getCategory());

        return statement.executeUpdate() != 0;
    }

    /**
     * update a product
     * 
     * @param id
     * @param name
     * @param description
     * @param cost
     * @param imagePath
     * @param categoryID
     */
    @Override
    public boolean update(Product object) throws SQLException {
        var statement = Request.Connection.getConnection()
                .prepareStatement("UPDATE `produit` SET `id_produit`= " + object.getId() + ",`nom`= " + object.getName()
                        + ",`description`= " + object.getDescription() + ",`tarif`= " + object.getCost() + ",`visuel`= "
                        + object.getImagePath() + ",`id_categorie`= " + object.getCategory() + " WHERE `id_produit` = "
                        + object.getId());

        return statement.executeUpdate() != 0;
    }

    /**
     * delete a product
     * 
     * @param id
     */

    @Override
    public boolean delete(Product object) throws SQLException {
        var statement = Request.Connection.getConnection()
                .prepareStatement("DELETE FROM `produit` WHERE `id_produit` = " + object.getId());

        return statement.executeUpdate() != 0;
    }

    @Override
    public Product getById(int id) throws SQLException {
        var statement = Request.Connection.getConnection().createStatement();
        var result = statement.executeQuery(
                "SELECT `id_produit`, `nom`, `description`, `tarif`, `visuel`, `id_categorie` WHERE `id_produit`="
                        + id);
        return result.next()
                ? new Product(result.getInt("id_produit"), result.getString("nom"), result.getDouble("tarif"),
                        result.getString("description"), result.getInt("id_categorie"), result.getString("visuel"))
                : null;
    }

    @Override
    public Product[] getAll() throws SQLException {

        final var statement = Request.Connection.getConnection().createStatement();
        final ResultSet result = statement.executeQuery(
                "SELECT `id_produit`, `nom`, `description`, `tarif`, `visuel`, `id_categorie` FROM `produit`");
        var productList = new LinkedList<Product>();
        while (result.next())
            productList.add(new Product(result.getInt("id_produit"), result.getString("nom"), result.getDouble("tarif"),
                    result.getString("description"), result.getInt("id_categorie"), result.getString("visuel")));
        return productList.size() > 0 ? productList.toArray(new Product[0]) : null;
    }
}
