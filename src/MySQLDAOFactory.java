/**
 * import dao.model.CategoryDAO; import dao.model.ProductDAO; import
 * dao.mySQL.*;
 * 
 * //faire toutes les requetes sql public class MySQLDAOFactory extends
 * DAOFactory {
 * 
 * @Override public CustomerDAO getCustomerDAO() { return
 *           MySQLCustomerDAO.getInstance(); }
 * 
 * @Override public ProductDAO getProductDAO() { return
 *           MySQLProductDAO.getInstance(); }
 * 
 * @Override public CategoryDAO getCategoryDAO() { return
 *           MySQLCategoryDAO.getInstance(); }
 * 
 *           }
 **/