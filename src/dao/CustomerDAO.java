package dao;

import model.Customer;

import java.sql.SQLException;

/**
 * Interface DAO utilise un type T cette class hérite de DAO, et l'objet de type
 * T se défini ici comme Customer
 */
public interface CustomerDAO extends DAO<Customer> {
    /**
     * Finds a customer by its id
     * 
     * @param id id of the customer
     * @return the customer found, or null for nothing
     */
    Customer getById(int id);

    /**
     * Returns every customers
     * 
     * @return every customers
     */
    Customer[] getAll();

}
