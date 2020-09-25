package dao.mySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import dao.OrderDAO;
import model.Order;
import model.OrderLine;
import model.Product;
import Request.*;

public class MySQLOrderDAO implements OrderDAO {

    // Instance of the class
    private static MySQLOrderDAO instance;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // constructor
    private MySQLOrderDAO() {
    }

    // Insure to get only 1 instance
    public static MySQLOrderDAO getInstance() {
        if (instance == null)
            instance = new MySQLOrderDAO();

        return instance;
    }

    @Override
    public boolean create(final Order object) throws SQLException {

        var statement = Request.Connection.getConnection()
                .prepareStatement("INSERT INTO `Commande` (`date_commande`, `id_client`) VALUES ("
                        + object.getDate().format(formatter) + ", " + +object.getCustomer() + ");");

        return statement.executeUpdate() != 0;

    }

    @Override
    public boolean update(final Order object) throws SQLException {

        var statement = Request.Connection.getConnection()
                .prepareStatement("UPDATE `Commande` SET `date_commande`= " + object.getDate().format(formatter)
                        + ",`id_client`=" + object.getCustomer() + " WHERE `id_commande` = " + object.getId());

        return statement.executeUpdate() != 0;
    }

    @Override
    public boolean delete(final Order object) throws SQLException {

        var statement = Request.Connection.getConnection()
                .prepareStatement("DELETE FROM `Commande` WHERE `id_commande` = " + object.getId());

        return statement.executeUpdate() != 0;
    }

    @Override
    public Order getById(final int id) throws SQLException {
        var statement = Request.Connection.getConnection().createStatement();
        var result = statement.executeQuery(
                "SELECT `id_commande`, `date_commande`, `id_client` FROM `commande` WHERE `id_commande`=" + id);
        return result.next()
                ? new Order(result.getInt("id_commande"), result.getDate("date_commande").toLocalDate(),
                        result.getInt("id_client"))
                : null;
    }

    @Override
    public Order[] getAll() throws SQLException {
        /*
         * var statement = Request.Connection.getConnection().createStatement(); var
         * result = statement.executeQuery(
         * "SELECT `id_produit`, `id_commande`, `quantite`, `tarif_unitaire` FROM `Ligne_commande` WHERE"
         * + "`id_produit`=" + product); var list = new LinkedList<OrderLine>(); while
         * (result.next()) list.add(new OrderLine(result.getInt("id_commande"),
         * result.getInt("id_produit"), result.getDouble("tarif_unitaire"),
         * result.getInt("quantite"))); return list.toArray(new OrderLine[0]);
         */
        return null;
    }

}
