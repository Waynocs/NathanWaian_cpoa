package tests.dao;

import dao.DAOFactory;
import model.Product;
import tests.dao.ProductTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public abstract class ProductTest {
    @Test
    public void testCreate() {
        var res = getFactory().getProductDAO()
                .create(new Product(-1, "test", 45, "voici une description", 1, "test.png"));
        assertNotNull(res, "object added");
        var res2 = getFactory().getProductDAO()
                .create(new Product(-1, "test", 45, "voici une description", 1, "test.png"));
        assertTrue(res.getId() != -1, "different ids for different ");
        getFactory().getProductDAO().delete(res);
        getFactory().getProductDAO().delete(res2);
    }

    @Test
    public void testUpdate() {
        var res = getFactory().getProductDAO()
                .create(new Product(0, "test", 45, "voici une description", 1, "test.png"));
        res.setName("new name");
        res.setImagePath("new image path");
        res.setDescription("new description");
        res.setCost(2);
        res.setCategory(2);
        getFactory().getProductDAO().update(res);
        var updated = getFactory().getProductDAO().getById(res.getId());
        assertTrue(updated.getName().equals("new name"), "name");
        assertTrue(updated.getImagePath().equals("new path"), "image path");
        getFactory().getProductDAO().delete(res);

    }

    @Test
    public void testDelete() {
        var res = getFactory().getProductDAO()
                .create(new Product(0, "test", 45, "voici une description", 1, "test.png"));
        assertTrue(getFactory().getProductDAO().delete(res));
        assertFalse(getFactory().getProductDAO().delete(res));
    }

    @Test
    public void testGetById() {
        var res = getFactory().getProductDAO()
                .create(new Product(0, "test", 45, "voici une description", 1, "test.png"));
        assertNotNull(getFactory().getProductDAO().getById(res.getId()), "get deleted");
        getFactory().getProductDAO().delete(res);
        assertNull(getFactory().getProductDAO().getById(res.getId()), "d");
        assertNull(getFactory().getProductDAO().getById(res.getId() + 1));

    }

    @Test
    public void testGetAll() {
        var init = getFactory().getProductDAO().getAll().length;
        var res = getFactory().getProductDAO()
                .create(new Product(0, "test", 45, "voici une description", 1, "test.png"));
        assertTrue(getFactory().getProductDAO().getAll().length == init + 1);
        getFactory().getProductDAO().delete(res);
    }

    public void testIntegrity() {
        var res = getFactory().getProductDAO()
                .create(new Product(0, "test", 45, "voici une description", 1, "test.png"));
        res.setName("new name");
        // the local object should NOT change the stored one without update()
        assertFalse(getFactory().getProductDAO().getById(res.getId()).getName().equals("new name"));
        getFactory().getProductDAO().delete(res);
    }

    protected abstract DAOFactory getFactory();
}
