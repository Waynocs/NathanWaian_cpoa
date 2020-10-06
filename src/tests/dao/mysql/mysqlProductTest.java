package tests.dao.mysql;

import dao.DAOFactory;
import dao.DAOFactory.Mode;
import tests.dao.ProductTest;

public class mysqlProductTest extends ProductTest {

    @Override
    protected DAOFactory getFactory() {
        return DAOFactory.getFactory(Mode.SQL);
    }

}