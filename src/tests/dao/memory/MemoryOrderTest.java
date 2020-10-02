package tests.dao.memory;

import dao.DAOFactory;
import dao.DAOFactory.Mode;
import tests.dao.OrderTest;

public class MemoryOrderTest extends OrderTest {

    @Override
    protected DAOFactory getFactory() {
        return DAOFactory.getFactory(Mode.MEMORY);
    }

}
