package dao;

import model.Category;

import java.sql.SQLException;

/**
 * Interface DAO utilise un type T cette class hérite de DAO, et l'objet de type
 * T se défini ici comme Category
 */
public interface CategoryDAO extends DAO<Category> {
    /**
     * Finds a category by its id
     * 
     * @param id id of the category
     * @return the category found, or null for nothing
     * @throws SQLException
     */
    Category getById(int id) throws SQLException;

    /**
     * Returns every categories
     * 
     * @return every categories
     * @throws SQLException
     */
    Category[] getAll() throws SQLException;

}