package dao;

import model.Product;

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
     * @exception DAOException thrown when bad stuff happened
     */
    Product getById(int id) throws DAOException;

    /**
     * Returns every products
     * 
     * @return every products
     * @exception DAOException thrown when bad stuff happened
     */
    Product[] getAll() throws DAOException;

}
