package src.view;

public class MemoryListDAOFactory {

    @Override
public EtudiantDAO getEtudiantDAO() {
return ListeMemoireEtudiantDAO.getInstance();
}
@Override
public PromoDAO getPromoDAO() {
return ListeMemoirePromoDAO.getInstance();
}


    
}
