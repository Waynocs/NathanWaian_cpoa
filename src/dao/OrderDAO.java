package dao;

import model.Order;

import java.sql.SQLException;

import dao.DAO;

/**
 * Interface DAO utilise un type T cette class hérite de DAO, et l'objet de type
 * T se défini ici comme Command
 */
public interface OrderDAO extends DAO<Order> {
    Order getById(int id) throws SQLException;

    Order[] getAll() throws SQLException;

}
