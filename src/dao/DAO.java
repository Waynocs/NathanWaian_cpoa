package dao;

import java.sql.SQLException;

//INTERFACE UTILISE UN OBJET DE TYPE T
public interface DAO<T> {
    /**
     * Creates a new element
     * 
     * @param object element to create
     * @return true if successful
     */
    boolean create(T object);

    /**
     * Updates an element
     * 
     * @param object element to update
     * @return true if successful
     */
    boolean update(T object);

    /**
     * Deletes an element
     * 
     * @param object element to delete
     * @return true if successful
     */
    boolean delete(T object);
}
