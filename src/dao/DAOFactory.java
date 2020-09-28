package dao;

import dao.Memory.MemoryDAOFactory;
import dao.mySQL.MySQLDAOFactory;

/**
 * base DAO factory
 */
public abstract class DAOFactory {
    /**
     * Mode used to create a DAO Factory
     */
    public enum Mode {
        /**
         * The SQL mode will output a factory using an SQL database
         */
        SQL,
        /**
         * The memory mode, only saved in the current running instance
         */
        MEMORY
    }

    /**
     * Returns a factory depending of the mode used, or null if the mode is
     * undefined
     * 
     * @param mode the mode will affect the way the factory saves its changes or
     *             connects to the data
     * @return a factory depending of the mode used
     */
    public static DAOFactory getFactory(Mode mode) {
        switch (mode) {
            case SQL:
                return MySQLDAOFactory.getInstance();
            case MEMORY:
                return MemoryDAOFactory.getInstance();
            default:
                return null;
        }
    }

    /**
     * Returns the Category component of the factory
     * 
     * @return the Category component of the factory
     */
    public abstract CategoryDAO getCategoryDAO();

    /**
     * Returns the Customer component of the factory
     * 
     * @return the Customer component of the factory
     */
    public abstract CustomerDAO getCustomerDAO();

    /**
     * Returns the Order component of the factory
     * 
     * @return the Order component of the factory
     */
    public abstract OrderDAO getOrderDAO();

    /**
     * Returns the Order Line component of the factory
     * 
     * @return the Order Line component of the factory
     */
    public abstract OrderLineDAO getOrderLineDAO();
}
