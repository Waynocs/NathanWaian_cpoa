package tests.dao;

import dao.DAOFactory;
import model.Order;
import tests.dao.OrderTest;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class OrderTest {

    @BeforeEach
    public void before() {
        var DAO = getFactory().getOrderDAO();
        for (var order : DAO.getAll())
            DAO.delete(order);
    }

    @Test
    public void testCreate() {
        var order1 = getFactory().getOrderDAO().create(new Order(-1, LocalDateTime.of(2020, 9, 1, 10, 30), 0));
        assertNotNull(order1, "create new order");
        var order2 = getFactory().getOrderDAO().create(new Order(-1, LocalDateTime.of(2020, 9, 1, 10, 30), 0));
        assertFalse(order1.getId() == order2.getId(), "different ids for different orders");
        getFactory().getOrderDAO().delete(order1);
        getFactory().getOrderDAO().delete(order2);
    }

    @Test
    public void testUpdate() {
        var res = getFactory().getOrderDAO().create(new Order(-1, LocalDateTime.of(2020, 9, 1, 10, 30), 0));
        res.setCustomer(45);
        res.setDate(LocalDateTime.of(2000, 1, 1, 1, 1));
        getFactory().getOrderDAO().update(res);
        var updated = getFactory().getOrderDAO().getById(res.getId());
        assertTrue(updated.getCustomer() == 45, "customer id");
        assertTrue(updated.getDate().equals(LocalDateTime.of(2000, 1, 1, 1, 1)), "date");
        getFactory().getOrderDAO().delete(res);
    }

    @Test
    public void testDelete() {
        var res = getFactory().getOrderDAO().create(new Order(-1, LocalDateTime.of(2020, 9, 1, 10, 30), 0));
        assertTrue(getFactory().getOrderDAO().delete(res), "delete existing");
        assertFalse(getFactory().getOrderDAO().delete(res), "delete already deleted");
        assertFalse(getFactory().getOrderDAO().delete(new Order(-1, LocalDateTime.of(2020, 9, 1, 10, 30), 0)),
                "delete non existing");
    }

    @Test
    public void testGetByID() {
        var res = getFactory().getOrderDAO().create(new Order(-1, LocalDateTime.of(2020, 9, 1, 10, 30), 0));
        assertNotNull(getFactory().getOrderDAO().getById(res.getId()), "get existing");
        getFactory().getOrderDAO().delete(res);
        assertNull(getFactory().getOrderDAO().getById(res.getId()), "get deleted");
        assertNull(getFactory().getOrderDAO().getById(res.getId() + 1), "get non existing");
    }

    @Test
    public void testGetAll() {
        var init = getFactory().getOrderDAO().getAll().length;
        var res = getFactory().getOrderDAO().create(new Order(-1, LocalDateTime.of(2020, 9, 1, 10, 30), 0));
        assertTrue(getFactory().getOrderDAO().getAll().length == init + 1);
        getFactory().getOrderDAO().delete(res);
    }

    @Test
    public void testIntegrity() {
        var res = getFactory().getOrderDAO().create(new Order(-1, LocalDateTime.of(2020, 9, 1, 10, 30), 0));
        res.setCustomer(45);
        // the local object should NOT change the stored one without update()
        assertFalse(getFactory().getOrderDAO().getById(res.getId()).getCustomer() == 45);
        getFactory().getOrderDAO().delete(res);
    }

    protected abstract DAOFactory getFactory();
}
