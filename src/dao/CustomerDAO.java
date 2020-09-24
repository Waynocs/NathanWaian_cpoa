package dao;

import model.Customer;

import java.sql.SQLException;

/**
 * Interface DAO utilise un type T cette class hérite de DAO, et l'objet de type
 * T se défini ici comme Customer
 */
public interface CustomerDAO extends DAO<Customer> {
    Customer getById(int id) throws SQLException;

    Customer[] getAll() throws SQLException;

}
