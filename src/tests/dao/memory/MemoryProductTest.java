package tests.dao.memory;

import dao.DAOFactory;
import dao.DAOFactory.Mode;
import tests.dao.ProductTest;

public class MemoryProductTest extends ProductTest {

    @Override
    protected DAOFactory getFactory() {
        return DAOFactory.getFactory(Mode.MEMORY);
    }

}
