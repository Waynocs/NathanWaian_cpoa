package tests.dao.mysql;

import dao.DAOFactory;
import dao.DAOFactory.Mode;
import tests.dao.CustomerTest;

public class mysqlCustomerTest extends CustomerTest {

    @Override
    protected DAOFactory getFactory() {
        return DAOFactory.getFactory(Mode.SQL);
    }

}