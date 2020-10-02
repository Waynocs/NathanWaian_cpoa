package tests.dao.mysql;

import dao.DAOFactory;
import dao.DAOFactory.Mode;
import tests.dao.OrderTest;

public class mysqlOrderTest extends OrderTest {
    @Override
    protected DAOFactory getFactory() {
        return DAOFactory.getFactory(Mode.SQL);
    }

}
