package dao;

import model.Category;

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
     */
    Category getById(int id);

    /**
     * Returns every categories
     * 
     * @return every categories
     */
    Category[] getAll();

}
