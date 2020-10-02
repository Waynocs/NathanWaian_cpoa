package dao.mySQL;

import java.sql.SQLException;
import java.util.LinkedList;

import dao.OrderLineDAO;
import model.OrderLine;
import request.Connection;

/**
 * Class used to manage order lines using the MySQLDAOFactory
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
    public OrderLine create(OrderLine object) {
        try {
            var statement = Connection.getConnection().createStatement();
            statement.executeUpdate(
                    "INSERT INTO `ligne_commande`(`id_commande`, `id_produit`, `quantite`, `tarif_unitaire`) VALUES ("
                            + object.getOrder() + ", " + +object.getProduct() + ", " + +object.getQuantity() + ", "
                            + +object.getCost() + ")");
            return object;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(OrderLine object) {
        try {
            var statement = Connection.getConnection().createStatement();
            return statement.executeUpdate("UPDATE `ligne_commande` SET `quantite`=" + object.getQuantity()
                    + ",`tarif_unitaire`=" + object.getCost() + " WHERE `id_produit`=" + object.getProduct()
                    + " AND `id_commande`=" + object.getOrder()) != 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(OrderLine object) {
        try {
            var statement = Connection.getConnection().createStatement();
            return statement.executeUpdate("DELETE FROM `ligne_commande` WHERE `id_produit`=" + object.getProduct()
                    + " AND `id_commande`=" + object.getOrder()) != 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public OrderLine getById(int order, int product) {
        try {
            var statement = Connection.getConnection().createStatement();
            var result = statement.executeQuery(
                    "SELECT `id_produit`, `id_commande`, `quantite`, `tarif_unitaire` FROM `ligne_commande` WHERE"
                            + "`id_produit`=" + product + " AND `id_commande`=" + order);
            return result.next()
                    ? new OrderLine(result.getInt("id_commande"), result.getInt("id_produit"),
                            result.getDouble("tarif_unitaire"), result.getInt("quantite"))
                    : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public OrderLine[] getAllFromOrder(int order) {
        try {
            var statement = Connection.getConnection().createStatement();
            var result = statement.executeQuery(
                    "SELECT `id_produit`, `id_commande`, `quantite`, `tarif_unitaire` FROM `ligne_commande` WHERE"
                            + "`id_commande`=" + order);
            var list = new LinkedList<OrderLine>();
            while (result.next())
                list.add(new OrderLine(result.getInt("id_commande"), result.getInt("id_produit"),
                        result.getDouble("tarif_unitaire"), result.getInt("quantite")));
            return list.toArray(new OrderLine[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new OrderLine[0];
        }
    }

    @Override
    public OrderLine[] getAllFromProduct(int product) {
        try {
            var statement = Connection.getConnection().createStatement();
            var result = statement.executeQuery(
                    "SELECT `id_produit`, `id_commande`, `quantite`, `tarif_unitaire` FROM `ligne_commande` WHERE"
                            + "`id_produit`=" + product);
            var list = new LinkedList<OrderLine>();
            while (result.next())
                list.add(new OrderLine(result.getInt("id_commande"), result.getInt("id_produit"),
                        result.getDouble("tarif_unitaire"), result.getInt("quantite")));
            return list.toArray(new OrderLine[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new OrderLine[0];
        }
    }

    @Override
    public OrderLine[] getAll() {
        try {
            var statement = Connection.getConnection().createStatement();
            var result = statement.executeQuery(
                    "SELECT `id_produit`, `id_commande`, `quantite`, `tarif_unitaire` FROM `ligne_commande`");
            var list = new LinkedList<OrderLine>();
            while (result.next())
                list.add(new OrderLine(result.getInt("id_commande"), result.getInt("id_produit"),
                        result.getDouble("tarif_unitaire"), result.getInt("quantite")));
            return list.toArray(new OrderLine[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new OrderLine[0];
        }
    }
}
