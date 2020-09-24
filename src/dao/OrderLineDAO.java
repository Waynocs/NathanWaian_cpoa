package dao;

import model.OrderLine;

import java.sql.SQLException;

/**
 * Interface DAO utilise un type T cette class hérite de DAO, et l'objet de type
 * T se défini ici comme OrderLine
 */
public interface OrderLineDAO extends DAO<OrderLine> {
    /**
     * Finds a order line by its order and product ids
     * 
     * @param order   id of the order
     * @param product id of the product
     * @return the line found, or null for nothing
     * @throws SQLException
     */
    OrderLine getById(int order, int product) throws SQLException;

    /**
     * Returns Finds all lines from a single order
     * 
     * @param order order to look for
     * @return all the lines from a single order
     * @throws SQLException
     */
    OrderLine[] getAllFromOrder(int order) throws SQLException;

    /**
     * Finds all lines from a single product
     * 
     * @param product product to look for
     * @return all the lines from a single product
     * @throws SQLException
     */
    OrderLine[] getAllFromProduct(int product) throws SQLException;

}
