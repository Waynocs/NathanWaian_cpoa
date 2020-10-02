package tests.dao;

import dao.DAOFactory;
import model.Category;
import tests.dao.CategoryTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public abstract class CategoryTest {
    @Test
    public void testCreate() {
        var res = getFactory().getCategoryDAO().create(new Category("test", "test.png", -1));
        assertNotNull(res, "object added");
        var res2 = getFactory().getCategoryDAO().create(new Category("test", "test.png", -1));
        assertTrue(res.getId() != res2.getId(), "different ids for different objects");
        getFactory().getCategoryDAO().delete(res);
        getFactory().getCategoryDAO().delete(res2);
    }

    @Test
    public void testUpdate() {
        var res = getFactory().getCategoryDAO().create(new Category("test", "test.png", 0));
        res.setName("new name");
        res.setImagePath("new path");
        getFactory().getCategoryDAO().update(res);
        var updated = getFactory().getCategoryDAO().getById(res.getId());
        assertTrue(updated.getName().equals("new name"), "name");
        assertTrue(updated.getImagePath().equals("new path"), "image path");
        getFactory().getCategoryDAO().delete(res);
    }

    @Test
    public void testDelete() {
        var res = getFactory().getCategoryDAO().create(new Category("test", "test.png", 0));
        assertTrue(getFactory().getCategoryDAO().delete(res), "delete existing");
        assertFalse(getFactory().getCategoryDAO().delete(res), "delete already deleted");
        assertFalse(getFactory().getCategoryDAO().delete(new Category("name", "imagePath", res.getId() + 1)),
                "delete non existing");
    }

    @Test
    public void testGetByID() {
        var res = getFactory().getCategoryDAO().create(new Category("test", "test.png", 0));
        assertNotNull(getFactory().getCategoryDAO().getById(res.getId()), "get existing");
        getFactory().getCategoryDAO().delete(res);
        assertNull(getFactory().getCategoryDAO().getById(res.getId()), "get deleted");
        assertNull(getFactory().getCategoryDAO().getById(res.getId() + 1), "get non existing");
    }

    @Test
    public void testGetAll() {
        var init = getFactory().getCategoryDAO().getAll().length;
        var res = getFactory().getCategoryDAO().create(new Category("test", "test.png", 0));
        assertTrue(getFactory().getCategoryDAO().getAll().length == init + 1);
        getFactory().getCategoryDAO().delete(res);
    }

    @Test
    public void testIntegrity() {
        var res = getFactory().getCategoryDAO().create(new Category("test", "test.png", 0));
        res.setName("new name");
        // the local object should NOT change the stored one without update()
        assertFalse(getFactory().getCategoryDAO().getById(res.getId()).getName().equals("new name"));
        getFactory().getCategoryDAO().delete(res);
    }

    protected abstract DAOFactory getFactory();
}
