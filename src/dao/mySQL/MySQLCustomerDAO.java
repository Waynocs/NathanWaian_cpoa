package dao.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import java.sql.Statement;

import dao.CustomerDAO;
import dao.DAOException;
import model.Customer;
import model.Order;
import request.Connection;

/**
 * Class used to manage customers using the MySQLDAOFactory
 */
public class MySQLCustomerDAO implements CustomerDAO {

    private static MySQLCustomerDAO instance;

    private MySQLCustomerDAO() {
    }

    /**
     * Returns the only instance of this class
     * 
     * @return the only instance of this class
     */
    public static MySQLCustomerDAO getInstance() {
        if (instance == null)
            instance = new MySQLCustomerDAO();

        return instance;
    }

    @Override
    public Customer create(final Customer object) {
        try {
            final var statement = Connection.getConnection().prepareStatement(
                    "INSERT INTO `client`(`nom`, `prenom`, `identifiant`, `mot_de_passe`, `adr_numero`, `adr_voie`, `adr_code_postal`, `adr_ville`, `adr_pays`) VALUES ('"
                            + object.getSurname() + "', '" + object.getName() + "', '" + object.getIdentifier() + "', '"
                            + object.getPwd() + "', '" + object.getAddressNumber() + "', '" + object.getAddressStreet()
                            + "', '" + object.getAddressPostalCode() + "', '" + object.getAddressCity() + "', '"
                            + object.getAddressCountry() + "')",
                    Statement.RETURN_GENERATED_KEYS);
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
    public boolean update(final Customer object) {
        try {
            final var statement = Connection.getConnection()
                    .prepareStatement("UPDATE `client` SET `nom`= '" + object.getSurname() + "',`prenom`= '"
                            + object.getName() + "',`identifiant`='" + object.getIdentifier() + "',`mot_de_passe`= '"
                            + object.getPwd() + "',`adr_numero`= '" + object.getAddressNumber() + "',`adr_voie`= '"
                            + object.getAddressStreet() + "',`adr_code_postal`= '" + object.getAddressPostalCode()
                            + "',`adr_ville`= '" + object.getAddressCity() + "',`adr_pays`= '"
                            + object.getAddressCountry() + "' WHERE `id_client` = " + object.getId());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public boolean delete(final Customer object) {
        try {
            for (Order order : MySQLOrderDAO.getInstance().getAll())
                if (order.getCustomer() == object.getId())
                    throw new DAOException("The customer '" + object.getId() + "' is used by an order");
            final var statement = Connection.getConnection()
                    .prepareStatement("DELETE FROM `client` WHERE `id_client` = " + object.getId());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Customer getById(final int id) {
        try {
            final var statement = Connection.getConnection().createStatement();
            final var result = statement.executeQuery(
                    "SELECT `id_client`, `nom`, `prenom`, `identifiant`, `mot_de_passe`, `adr_numero`, `adr_voie`, `adr_code_postal`, `adr_ville`, `adr_pays` FROM `client` WHERE `id_client`="
                            + id);
            return result.next() ? new Customer(result.getInt("id_client"), result.getString("prenom"),
                    result.getString("nom"), result.getString("identifiant"), result.getString("mot_de_passe"),
                    result.getString("adr_numero"), result.getString("adr_voie"), result.getString("adr_code_postal"),
                    result.getString("adr_ville"), result.getString("adr_pays")) : null;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Customer[] getAll() {
        try {
            final var statement = Connection.getConnection().createStatement();
            final ResultSet result = statement.executeQuery(
                    "SELECT `id_client`, `nom`, `prenom`, `identifiant`, `mot_de_passe`, `adr_numero`, `adr_voie`, `adr_code_postal`, `adr_ville`, `adr_pays` FROM `client`");
            var customerList = new LinkedList<Customer>();
            while (result.next())
                customerList.add(new Customer(result.getInt("id_client"), result.getString("prenom"),
                        result.getString("nom"), result.getString("identifiant"), result.getString("mot_de_passe"),
                        result.getString("adr_numero"), result.getString("adr_voie"),
                        result.getString("adr_code_postal"), result.getString("adr_ville"),
                        result.getString("adr_pays")));
            return customerList.toArray(new Customer[0]);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }
}
