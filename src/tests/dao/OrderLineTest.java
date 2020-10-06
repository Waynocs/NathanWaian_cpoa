package tests.dao;

import dao.DAOFactory;
import model.OrderLine;
import tests.dao.OrderLineTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class OrderLineTest {

    @BeforeEach
    public void before() {
        var DAO = getFactory().getOrderLineDAO();
        for (var order : DAO.getAll())
            DAO.delete(order);
    }

    @Test
    public void testCreate() {
        var order1 = getFactory().getOrderLineDAO().create(new OrderLine(-1, -1, 5.95, 2));
        assertNotNull(order1, "create new order");
        var order2 = getFactory().getOrderLineDAO().create(new OrderLine(-1, -1, 5.95, 2));
        assertNull(order2, "create identical order");
    }

    @Test
    public void testUpdate() {
        var res = getFactory().getOrderLineDAO().create(new OrderLine(-1, -1, 5.95, 2));
        res.setCost(45);
        res.setQuantity(12);
        getFactory().getOrderLineDAO().update(res);
        var updated = getFactory().getOrderLineDAO().getById(res.getOrder(), res.getProduct());
        assertTrue(updated.getCost() == 45, "cost");
        assertTrue(updated.getProduct() == 12, "quantity");
    }

    @Test
    public void testDelete() {
        var res = getFactory().getOrderLineDAO().create(new OrderLine(-1, -1, 5.95, 2));
        assertTrue(getFactory().getOrderLineDAO().delete(res), "delete existing");
        assertFalse(getFactory().getOrderLineDAO().delete(res), "delete already deleted");
        assertFalse(getFactory().getOrderLineDAO().delete(new OrderLine(-1, -1, 5.95, 2)), "delete non existing");
    }

    @Test
    public void testGetByID() {
        var res = getFactory().getOrderLineDAO().create(new OrderLine(-1, -1, 5.95, 2));
        assertNotNull(getFactory().getOrderLineDAO().getById(res.getOrder(), res.getProduct()), "get existing");
        getFactory().getOrderLineDAO().delete(res);
        assertNull(getFactory().getOrderLineDAO().getById(res.getOrder(), res.getProduct()), "get deleted");
        assertNull(getFactory().getOrderLineDAO().getById(res.getOrder() + 1, res.getProduct() + 1),
                "get non existing");
    }

    @Test
    public void testGetAll() {
        var list = new ArrayList<OrderLine>();
        list.add(getFactory().getOrderLineDAO().create(new OrderLine(-1, -1, 5.95, 2)));
        list.add(getFactory().getOrderLineDAO().create(new OrderLine(-1, -1, 8, 4)));
        list.add(getFactory().getOrderLineDAO().create(new OrderLine(-1, -1, 3, 6)));
        var all = getFactory().getOrderLineDAO().getAll();
        assertTrue(all.length == 3);
        for (OrderLine orderLine : all)
            assertTrue(list.contains(orderLine));
    }

    @Test
    public void testIntegrity() {
        var res = getFactory().getOrderLineDAO().create(new OrderLine(-1, -1, 5.95, 2));
        res.setCost(45);
        // the local object should NOT change the stored one without update()
        assertFalse(getFactory().getOrderLineDAO().getById(res.getOrder(), res.getProduct()).getCost() == 45);
    }

    protected abstract DAOFactory getFactory();
}
