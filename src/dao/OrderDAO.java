package dao;

import model.Order;

import java.sql.SQLException;

/**
 * Interface DAO utilise un type T cette class hérite de DAO, et l'objet de type
 * T se défini ici comme Command
 */
public interface OrderDAO extends DAO<Order> {
    /**
     * Finds an order by its id
     * 
     * @param id id of the order
     * @return the order found, or null for nothing
     * @throws SQLException
     */
    Order getById(int id) throws SQLException;

    /**
     * Returns every orders
     * 
     * @return every orders
     * @throws SQLException
     */
    Order[] getAll() throws SQLException;

}
