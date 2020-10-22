package dao;

//INTERFACE UTILISE UN OBJET DE TYPE T
public interface DAO<T> {
    /**
     * Creates a new element
     * 
     * @param object element to create
     * @return the given object, or null if it failed
     * @exception DAOException thrown when bad stuff happened
     */
    T create(T object) throws DAOException;

    /**
     * Updates an element
     * 
     * @param object element to update
     * @return true if successful
     * @exception DAOException thrown when bad stuff happened
     */
    boolean update(T object) throws DAOException;

    /**
     * Deletes an element
     * 
     * @param object element to delete
     * @return true if successful
     * @exception DAOException thrown when bad stuff happened
     */
    boolean delete(T object) throws DAOException;
}
