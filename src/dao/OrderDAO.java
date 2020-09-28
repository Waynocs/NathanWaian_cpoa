package dao;

import model.Order;

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
     */
    Order getById(int id);

    /**
     * Returns every orders
     * 
     * @return every orders
     */
    Order[] getAll();

}
