package dao;

import model.OrderLine;

import java.sql.SQLException;

import dao.DAO;

/**
 * Interface DAO utilise un type T cette class hérite de DAO, et l'objet de type
 * T se défini ici comme OrderLine
 */
public interface OrderLineDAO extends DAO<OrderLine> {
    OrderLine getById(int order, int product) throws SQLException;

    OrderLine[] getAllFromOrder(int order) throws SQLException;

    OrderLine[] getAllFromProduct(int product) throws SQLException;

}
