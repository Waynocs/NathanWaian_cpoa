package dao;

import model.Product;

import java.sql.SQLException;

/**
 * Interface DAO utilise un type T cette class hérite de DAO, et l'objet de type
 * T se défini ici comme Product
 */
public interface ProductDAO extends DAO<Product> {
    Product getById(int id) throws SQLException;

    Product[] getAll() throws SQLException;

}
