package dao.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import dao.CustomerDAO;
import model.Customer;

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
    public boolean create(final Customer object) {
        try {
            final var statement = Request.Connection.getConnection().prepareStatement(
                    "INSERT INTO `client`(`id_client`, `nom`, `prenom`, `identifiant`, `mot_de_passe`, `adr_numero`, `adr_voie`, `adr_code_postal`, `adr_ville`, `adr_pays`) VALUES ("
                            + object.getId() + ", " + object.getName() + ", " + object.getSurname() + ", "
                            + object.getIdentifier() + ", " + object.getPwd() + ", " + object.getAddressNumber() + ", "
                            + object.getAddressStreet() + ", " + object.getAddressPostalCode() + ", "
                            + object.getAddressCity() + ", " + object.getAddressCountry());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean update(final Customer object) {
        try {
            final var statement = Request.Connection.getConnection()
                    .prepareStatement("UPDATE `client` SET `id_client`= " + object.getId() + ",`nom`= "
                            + object.getName() + ",`prenom`= " + object.getSurname() + ",`identifiant`="
                            + object.getIdentifier() + "`mot_de_passe`= " + object.getPwd() + "`adr_numero`= "
                            + object.getAddressNumber() + "`adr_voie`= " + object.getAddressStreet()
                            + "`adr_code_postal`= " + object.getAddressPostalCode() + "`adr_ville`= "
                            + object.getAddressCity() + "`adr_pays`= " + object.getAddressCountry()
                            + " WHERE `id_client` = " + object.getId());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(final Customer object) {
        try {
            final var statement = Request.Connection.getConnection()
                    .prepareStatement("DELETE FROM `client` WHERE `id_client` = " + object.getId());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Customer getById(final int id) {
        try {
            final var statement = Request.Connection.getConnection().createStatement();
            final var result = statement.executeQuery(
                    "SELECT `id_client`, `nom`, `prenom`, `identifiant`, `mot_de_passe`, `adr_numero`, `adr_voie`, `adr_code_postal`, `adr_ville`, `adr_pays` FROM `client` WHERE `id_client`="
                            + id);
            return result.next() ? new Customer(result.getInt("id_client"), result.getString("nom"),
                    result.getString("prenom"), result.getString("identifiant"), result.getString("mot_de_passe"),
                    result.getString("adr_numero"), result.getString("adr_voie"), result.getString("adr_code_postal"),
                    result.getString("adr_ville"), result.getString("adr_pays")) : null;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Customer[] getAll() {
        try {
            final var statement = Request.Connection.getConnection().createStatement();
            final ResultSet result = statement.executeQuery(
                    "SELECT `id_client`, `nom`, `prenom`, `identifiant`, `mot_de_passe`, `adr_numero`, `adr_voie`, `adr_code_postal`, `adr_ville`, `adr_pays` FROM `client`");
            var customerList = new LinkedList<Customer>();
            while (result.next())
                customerList.add(new Customer(result.getInt("id_client"), result.getString("nom"),
                        result.getString("prenom"), result.getString("identifiant"), result.getString("mot_de_passe"),
                        result.getString("adr_numero"), result.getString("adr_voie"),
                        result.getString("adr_code_postal"), result.getString("adr_ville"),
                        result.getString("adr_pays")));
            return customerList.toArray(new Customer[0]);
        } catch (SQLException e) {
            return new Customer[0];
        }
    }
}
