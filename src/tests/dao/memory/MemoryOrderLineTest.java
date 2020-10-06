package tests.dao.memory;

import dao.DAOFactory;
import dao.DAOFactory.Mode;
import tests.dao.OrderLineTest;

public class MemoryOrderLineTest extends OrderLineTest {

    @Override
    protected DAOFactory getFactory() {
        return DAOFactory.getFactory(Mode.MEMORY);
    }

}
