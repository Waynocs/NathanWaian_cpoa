package dao.mySQL;

import java.sql.SQLException;
import java.util.LinkedList;

import dao.OrderLineDAO;
import model.OrderLine;

/**
 * OrderLineDAO with the SQL mode
 */
public class MySQLOrderLineDAO implements OrderLineDAO {

    private static MySQLOrderLineDAO instance;

    private MySQLOrderLineDAO() {
    }

    /**
     * Returns the only instance of this class
     * 
     * @return the only instance of this class
     */
    public static MySQLOrderLineDAO getInstance() {
        if (instance == null)
            instance = new MySQLOrderLineDAO();

        return instance;
    }

    @Override
    public boolean create(OrderLine object) {
        try {
            var statement = Request.Connection.getConnection().createStatement();
            return statement.executeUpdate(
                    "INSERT INTO `Ligne_commande`(`id_commande`, `id_produit`, `quantite`, `tarif_unitaire`) VALUES ("
                            + object.getOrder() + ", " + +object.getProduct() + ", " + +object.getQuantity() + ", "
                            + +object.getCost() + ")") != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean update(OrderLine object) {
        try {
            var statement = Request.Connection.getConnection().createStatement();
            return statement.executeUpdate("UPDATE `Ligne_commande` SET `quantite`=" + object.getQuantity()
                    + ",`tarif_unitaire`=" + object.getCost() + " WHERE `id_produit`=" + object.getProduct()
                    + " AND `id_commande`=" + object.getOrder()) != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(OrderLine object) {
        try {
            var statement = Request.Connection.getConnection().createStatement();
            return statement.executeUpdate("DELETE FROM `Ligne_commande` WHERE `id_produit`=" + object.getProduct()
                    + " AND `id_commande`=" + object.getOrder()) != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public OrderLine getById(int order, int product) {
        try {
            var statement = Request.Connection.getConnection().createStatement();
            var result = statement.executeQuery(
                    "SELECT `id_produit`, `id_commande`, `quantite`, `tarif_unitaire` FROM `Ligne_commande` WHERE"
                            + "`id_produit`=" + product + " AND `id_commande`=" + order);
            return result.next()
                    ? new OrderLine(result.getInt("id_commande"), result.getInt("id_produit"),
                            result.getDouble("tarif_unitaire"), result.getInt("quantite"))
                    : null;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public OrderLine[] getAllFromOrder(int order) {
        try {
            var statement = Request.Connection.getConnection().createStatement();
            var result = statement.executeQuery(
                    "SELECT `id_produit`, `id_commande`, `quantite`, `tarif_unitaire` FROM `Ligne_commande` WHERE"
                            + "`id_commande`=" + order);
            var list = new LinkedList<OrderLine>();
            while (result.next())
                list.add(new OrderLine(result.getInt("id_commande"), result.getInt("id_produit"),
                        result.getDouble("tarif_unitaire"), result.getInt("quantite")));
            return list.toArray(new OrderLine[0]);
        } catch (SQLException e) {
            return new OrderLine[0];
        }
    }

    @Override
    public OrderLine[] getAllFromProduct(int product) {
        try {
            var statement = Request.Connection.getConnection().createStatement();
            var result = statement.executeQuery(
                    "SELECT `id_produit`, `id_commande`, `quantite`, `tarif_unitaire` FROM `Ligne_commande` WHERE"
                            + "`id_produit`=" + product);
            var list = new LinkedList<OrderLine>();
            while (result.next())
                list.add(new OrderLine(result.getInt("id_commande"), result.getInt("id_produit"),
                        result.getDouble("tarif_unitaire"), result.getInt("quantite")));
            return list.toArray(new OrderLine[0]);
        } catch (SQLException e) {
            return new OrderLine[0];
        }
    }
}
