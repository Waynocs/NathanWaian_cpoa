package dao;

import model.Customer;

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
     * @exception DAOException thrown when bad stuff happened
     */
    Customer getById(int id) throws DAOException;

    /**
     * Returns every customers
     * 
     * @return every customers
     * @exception DAOException thrown when bad stuff happened
     */
    Customer[] getAll() throws DAOException;

}
