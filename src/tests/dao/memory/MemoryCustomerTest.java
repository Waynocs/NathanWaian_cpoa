package tests.dao.memory;

import dao.DAOFactory;
import dao.DAOFactory.Mode;
import tests.dao.CustomerTest;

public class MemoryCustomerTest extends CustomerTest {

    @Override
    protected DAOFactory getFactory() {
        return DAOFactory.getFactory(Mode.MEMORY);
    }

}
