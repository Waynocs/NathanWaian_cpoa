/**
 * public abstract class DAOFactory {
 * 
 * public static DAOFactory getDAOFactory(Persistance cible) { DAOFactory daoF =
 * null; switch (cible) { case MYSQL: daoF = new MySQLDAOFactory(); break; case
 * ListeMemoire: daoF = new MemoryListDAOFactory(); break; } return daoF; }
 * public abstract CustomerDAO getCustomerDAO(); public abstract ProductDAO
 * getProductDAO(); public abstract CategoryDAO getCategoryDAO();
 * 
 * }
 * 
 * }
 **/