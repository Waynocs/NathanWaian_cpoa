package dao.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import dao.OrderDAO;
import model.Order;

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

    /**
     * create a new order
     * 
     * @param id
     * @param date
     * @param customer
     */

    @Override
    public boolean create(final Order object) throws SQLException {

        var statement = Request.Connection.getConnection()
                .prepareStatement("INSERT INTO `commande` (`date_commande`, `id_client`) VALUES ("
                        + object.getDate().format(formatter) + ", " + +object.getCustomer() + ");");

        return statement.executeUpdate() != 0;

    }

    /**
     * update an order
     * 
     * @param id
     * @param date
     * @param customer
     */

    @Override
    public boolean update(final Order object) throws SQLException {

        var statement = Request.Connection.getConnection()
                .prepareStatement("UPDATE `commande` SET `date_commande`= " + object.getDate().format(formatter)
                        + ",`id_client`=" + object.getCustomer() + " WHERE `id_commande` = " + object.getId());

        return statement.executeUpdate() != 0;
    }

    /**
     * delete an order
     * 
     * @param id
     */

    @Override
    public boolean delete(final Order object) throws SQLException {

        var statement = Request.Connection.getConnection()
                .prepareStatement("DELETE FROM `commande` WHERE `id_commande` = " + object.getId());

        return statement.executeUpdate() != 0;
    }

    @Override
    public Order getById(final int id) throws SQLException {
<<<<<<< HEAD

=======
>>>>>>> f67daae1e0d713d3718398e1ab048fd3f5c78308
        var statement = Request.Connection.getConnection().createStatement();
        var result = statement.executeQuery(
                "SELECT `id_commande`, `date_commande`, `id_client` FROM `commande` WHERE `id_commande`=" + id);
        return result.next()
                ? new Order(result.getInt("id_commande"), result.getTimestamp("date_commande").toLocalDateTime(),
                        result.getInt("id_client"))
                : null;
<<<<<<< HEAD

=======
>>>>>>> f67daae1e0d713d3718398e1ab048fd3f5c78308
    }

    @Override
    public Order[] getAll() throws SQLException {

        final var statement = Request.Connection.getConnection().createStatement();
        final ResultSet result = statement
                .executeQuery("SELECT `id_commande`, `date_commande`, `id_client` FROM `commande`");
        var orderList = new LinkedList<Order>();
        while (result.next())
            orderList.add(new Order(result.getInt("id_commande"),
                    result.getTimestamp("date_commande").toLocalDateTime(), result.getInt("id_client")));
        return orderList.size() > 0 ? orderList.toArray(new Order[0]) : null;

    }

}
