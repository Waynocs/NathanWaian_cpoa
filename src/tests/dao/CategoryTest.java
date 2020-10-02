package tests.dao;

import dao.DAOFactory;
import dao.DAOFactory.Mode;
import model.Category;
import tests.dao.CategoryTest;
import dao.memory.MemoryCategoryDAO;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public abstract class CategoryTest {
    @Test
    public void testCreate() {
        var res = getFactory().getCategoryDAO().create(new Category("test", "test.png", 0));
        assertNotNull(res);
        getFactory().getCategoryDAO().delete(res);
    }

    @Test
    public void testUpdate() {
        var res = getFactory().getCategoryDAO().create(new Category("test", "test.png", 0));

    }

    protected abstract DAOFactory getFactory();
}
