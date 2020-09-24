package dao;

import java.sql.SQLException;

//INTERFACE UTILISE UN OBJET DE TYPE T
public interface DAO<T> {

    boolean create(T object) throws SQLException;

    boolean update(T object) throws SQLException;

    boolean delete(T object) throws SQLException;

    T getById(int id) throws SQLException;

    T[] getAll() throws SQLException;
}
