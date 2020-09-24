package dao;

import model.Category;

import java.sql.SQLException;

/**
 * Interface DAO utilise un type T cette class hérite de DAO, et l'objet de type
 * T se défini ici comme Category
 */
public interface CategoryDAO extends DAO<Category> {
    Category getById(int id) throws SQLException;

    Category[] getAll() throws SQLException;

}
