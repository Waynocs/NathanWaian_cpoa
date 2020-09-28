package dao;

import model.Product;

import java.sql.SQLException;

/**
 * Interface DAO utilise un type T cette class hérite de DAO, et l'objet de type
 * T se défini ici comme Product
 */
public interface ProductDAO extends DAO<Product> {
    /**
     * Finds a product by its id
     * 
     * @param id id of the product
     * @return the product found, or null for nothing
     */
    Product getById(int id);

    /**
     * Returns every products
     * 
     * @return every products
     */
    Product[] getAll();

}
