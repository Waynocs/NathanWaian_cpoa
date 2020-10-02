package tests.dao.memory;

import dao.DAOFactory;
import dao.DAOFactory.Mode;
import tests.dao.CategoryTest;

public class MemoryCategoryTest extends CategoryTest {

    @Override
    protected DAOFactory getFactory() {
        return DAOFactory.getFactory(Mode.MEMORY);
    }

}
