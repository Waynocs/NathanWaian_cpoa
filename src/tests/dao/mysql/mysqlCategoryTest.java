package tests.dao.mysql;

import dao.DAOFactory;
import dao.DAOFactory.Mode;
import tests.dao.CategoryTest;

public class mysqlCategoryTest extends CategoryTest {

    @Override
    protected DAOFactory getFactory() {
        return DAOFactory.getFactory(Mode.SQL);
    }

}
