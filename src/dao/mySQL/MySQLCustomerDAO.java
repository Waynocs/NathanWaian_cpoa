package dao.mySQL;

import java.sql.SQLException;

import dao.CustomerDAO;
import model.Customer;

public class MySQLCustomerDAO implements CustomerDAO {

    // Instance of the class
    private static MySQLCustomerDAO instance;

    // constructor
    private MySQLCustomerDAO() {
    }

    // Insure to get only 1 instance
    public static MySQLCustomerDAO getInstance() {
        if (instance == null)
            instance = new MySQLCustomerDAO();

        return instance;
    }

    /**
     * create a new customer
     * 
     * @param id
     * @param name
     * @param surname
     * @param identifier
     * @param pwd
     * @param adressNumber
     * @param adressStreet
     * @param adressPostalCode
     * @param adressCity
     * @param adressCountry
     */

    @Override
    public boolean create(Customer object) throws SQLException {
        var statement = Request.Connection.getConnection().prepareStatement(
                "INSERT INTO `client`(`id_client`, `nom`, `prenom`, `identifiant`, `mot_de_passe`, `adr_numero`, `adr_voie`, `adr_code_postal`, `adr_ville`, `adr_pays`) VALUES ("
                        + object.getId() + ", " + object.getName() + ", " + object.getSurname() + ", "
                        + object.getIdentifier() + ", " + object.getPwd() + ", " + object.getAddressNumber() + ", "
                        + object.getAddressStreet() + ", " + object.getAddressPostalCode() + ", "
                        + object.getAddressCity() + ", " + object.getAddressCountry());

        return statement.executeUpdate() != 0;

    }

    /**
     * update a customer
     * 
     * @param id
     * @param name
     * @param surname
     * @param identifier
     * @param pwd
     * @param adressNumber
     * @param adressStreet
     * @param adressPostalCode
     * @param adressCity
     * @param adressCountry
     */

    @Override
    public boolean update(Customer object) throws SQLException {
        var statement = Request.Connection.getConnection()
                .prepareStatement("UPDATE `client` SET `id_client`= " + object.getId() + ",`nom`= " + object.getName()
                        + ",`prenom`= " + object.getSurname() + ",`identifiant`=" + object.getIdentifier()
                        + "`mot_de_passe`= " + object.getPwd() + "`adr_numero`= " + object.getAddressNumber()
                        + "`adr_voie`= " + object.getAddressStreet() + "`adr_code_postal`= "
                        + object.getAddressPostalCode() + "`adr_ville`= " + object.getAddressCity() + "`adr_pays`= "
                        + object.getAddressCountry() + " WHERE `id_client` = " + object.getId());

        return statement.executeUpdate() != 0;

    }

    /**
     * delete a customer
     * 
     * @param id
     */

    @Override
    public boolean delete(Customer object) throws SQLException {
        var statement = Request.Connection.getConnection()
                .prepareStatement("DELETE FROM `client` WHERE `id_client` = " + object.getId());

        return statement.executeUpdate() != 0;
    }

    @Override
    public Customer getById(int id) throws SQLException {
        var statement = Request.Connection.getConnection().createStatement();
        var result = statement.executeQuery(
                "SELECT `id_client`, `nom`, `prenom`, `identifiant`, `mot_de_passe`, `adr_numero`, `adr_voie`, `adr_code_postal`, `adr_ville`, `adr_pays` FROM `client` WHERE `id_client`="
                        + id);
        return result.next() ? new Customer(result.getInt("id_client"), result.getString("nom"),
                result.getString("prenom"), result.getString("identifiant"), result.getString("mot_de_passe"),
                result.getString("adr_numero"), result.getString("adr_voie"), result.getString("adr_code_postal"),
                result.getString("adr_ville"), result.getString("adr_pays")) : null;
    }

    @Override
    public Customer[] getAll() throws SQLException {
        return null;
    }
}
