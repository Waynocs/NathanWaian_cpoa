package dao;

import java.sql.SQLException;

//INTERFACE UTILISE UN OBJET DE TYPE T
public interface DAO<T> {
    /**
     * Creates a new element
     * 
     * @param object element to create
     * @return true if successful
     * @throws SQLException
     */
    boolean create(T object) throws SQLException;

    /**
     * Updates an element
     * 
     * @param object element to update
     * @return true if successful
     * @throws SQLException
     */
    boolean update(T object) throws SQLException;

    /**
     * Deletes an element
     * 
     * @param object element to delete
     * @return true if successful
     * @throws SQLException
     */
    boolean delete(T object) throws SQLException;
}
