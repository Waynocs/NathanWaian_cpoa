package tests.dao.mysql;

import dao.DAOFactory;
import dao.DAOFactory.Mode;
import tests.dao.OrderLineTest;

public class mysqlOrderLineTest extends OrderLineTest {

    @Override
    protected DAOFactory getFactory() {
        return DAOFactory.getFactory(Mode.SQL);
    }

}