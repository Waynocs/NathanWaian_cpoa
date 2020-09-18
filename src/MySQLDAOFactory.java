package src.view;

public class MySQLDAOFactory extends DAOFactory {
    
    @Override
public EtudiantDAO getEtudiantDAO() {
return MySQLEtudiantDAO.getInstance();
}
@Override
public PromoDAO getPromoDAO() {
return MySQLPromoDAO.getInstance();
}


}
