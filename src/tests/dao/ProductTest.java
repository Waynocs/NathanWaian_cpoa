package tests.dao;

import dao.DAOFactory;
import model.Product;
import tests.dao.ProductTest;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class ProductTest {

    @BeforeEach
    public void before() {
        var DAO = getFactory().getProductDAO();
        for (var order : DAO.getAll())
            DAO.delete(order);
    }

    @Test
    public void testCreate() {
        var product1 = getFactory().getProductDAO()
                .create(new Product(-1, "test", 45, "voici une description", 1, "test.png"));
        assertNotNull(product1, "object added");
        var product2 = getFactory().getProductDAO()
                .create(new Product(-1, "test", 45, "voici une description", 1, "test.png"));
        assertTrue(product1.getId() != product2.getId(), "different ids for different Products");
        getFactory().getProductDAO().delete(product1);
        getFactory().getProductDAO().delete(product2);
    }

    @Test
    public void testUpdate() {
        var res = getFactory().getProductDAO()
                .create(new Product(0, "test", 45, "voici une description", 1, "test.png"));
        res.setName("new name");
        res.setCost(30.5);
        res.setDescription("new description");
        res.setCategory(2);
        res.setImagePath("new path");
        getFactory().getProductDAO().update(res);
        var updated = getFactory().getProductDAO().getById(res.getId());
        assertTrue(updated.getName().equals("new name"), "name");
        assertTrue(updated.getCost() == 30.5, "cost");
        assertTrue(updated.getDescription().equals("new description"), "description");
        assertTrue(updated.getCategory() == 2, "category");
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
        assertNull(getFactory().getProductDAO().getById(res.getId()), "get deleted");
        assertNull(getFactory().getProductDAO().getById(res.getId() + 1), "get non existing");

    }

    @Test
    public void testGetAll() {
        var init = getFactory().getProductDAO().getAll().length;
        var res = getFactory().getProductDAO()
                .create(new Product(0, "test", 45, "voici une description", 1, "test.png"));
        assertTrue(getFactory().getProductDAO().getAll().length == init + 1);
        getFactory().getProductDAO().delete(res);
    }

    @Test
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
