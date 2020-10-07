package tests.dao;

import dao.DAOFactory;
import model.Customer;
import tests.dao.CustomerTest;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class CustomerTest {

    @BeforeEach
    public void before() {
        var DAO = getFactory().getCustomerDAO();
        for (var order : DAO.getAll())
            DAO.delete(order);
    }

    @Test
    public void testCreate() {
        var customer1 = getFactory().getCustomerDAO().create(new Customer(-1, "name", "surname", "identifier", "pwd",
                "addressNumber", "addressStreet", "addressPostalCode", "addressCity", "addressCountry"));
        assertNotNull(customer1, "object added");
        var customer2 = getFactory().getCustomerDAO().create(new Customer(-1, "name", "surname", "identifier", "pwd",
                "addressNumber", "addressStreet", "addressPostalCode", "addressCity", "addressCountry"));
        assertFalse(customer1.getId() == customer2.getId(), "different ids for different Customers");
        getFactory().getCustomerDAO().delete(customer1);
        getFactory().getCustomerDAO().delete(customer2);
    }

    @Test
    public void testUpdate() {
        var res = getFactory().getCustomerDAO().create(new Customer(0, "name", "surname", "identifier", "pwd",
                "addressNumber", "addressStreet", "addressPostalCode", "addressCity", "addressCountry"));
        res.setName("new name");
        res.setSurname("new surname");
        res.setIdentifier("new identifier");
        res.setPwd("new pwd");
        res.setAddressNumber("new addressStreet");
        res.setAddressStreet("new addressNumber");
        res.setAddressPostalCode("new addressPostalCode");
        res.setAddressCity("new addressCity");
        res.setAddressCountry("new addressCountry");
        getFactory().getCustomerDAO().update(res);
        var updated = getFactory().getCustomerDAO().getById(res.getId());
        assertTrue(updated.getName().equals("new name"), "name");
        assertTrue(updated.getSurname().equals("new surname"), "surname");
        assertTrue(updated.getIdentifier().equals("new identifier"), "identifier");
        assertTrue(updated.getPwd().equals("new pwd"), "pwd");
        System.out.println(updated.getAddressNumber());
        assertTrue(updated.getAddressNumber().equals("new addressNumber"), "addressNumber");
        assertTrue(updated.getAddressStreet().equals("new addressStreet"), "addressStreet");
        assertTrue(updated.getAddressPostalCode().equals("new addressPostalCode"), "addressPostalCode");
        assertTrue(updated.getAddressCity().equals("new addressCity"), "addressCity");
        assertTrue(updated.getAddressCountry().equals("new addressCountry"), "addressCountry");
        getFactory().getCustomerDAO().delete(res);

    }

    @Test
    public void testDelete() {
        var res = getFactory().getCustomerDAO().create(new Customer(0, "name", "surname", "identifier", "pwd",
                "addressNumber", "addressStreet", "addressPostalCode", "addressCity", "addressCountry"));
        assertTrue(getFactory().getCustomerDAO().delete(res));
        assertFalse(getFactory().getCustomerDAO().delete(res));
    }

    @Test
    public void testGetById() {
        var res = getFactory().getCustomerDAO().create(new Customer(0, "name", "surname", "identifier", "pwd",
                "addressNumber", "addressStreet", "addressPostalCode", "addressCity", "addressCountry"));
        assertNotNull(getFactory().getCustomerDAO().getById(res.getId()), "get deleted");
        getFactory().getCustomerDAO().delete(res);
        assertNull(getFactory().getCustomerDAO().getById(res.getId()), "get deleted");
        assertNull(getFactory().getCustomerDAO().getById(res.getId() + 1), "get non existing");

    }

    @Test
    public void testGetAll() {
        var init = getFactory().getCustomerDAO().getAll().length;
        var res = getFactory().getCustomerDAO().create(new Customer(0, "name", "surname", "identifier", "pwd",
                "addressNumber", "addressStreet", "addressPostalCode", "addressCity", "addressCountry"));
        assertTrue(getFactory().getCustomerDAO().getAll().length == init + 1);
        getFactory().getCustomerDAO().delete(res);
    }

    @Test
    public void testIntegrity() {
        var res = getFactory().getCustomerDAO().create(new Customer(0, "name", "surname", "identifier", "pwd",
                "addressNumber", "addressStreet", "addressPostalCode", "addressCity", "addressCountry"));
        res.setName("new name");
        // the local object should NOT change the stored one without update()
        assertFalse(getFactory().getCustomerDAO().getById(res.getId()).getName().equals("new name"));
        getFactory().getCustomerDAO().delete(res);
    }

    protected abstract DAOFactory getFactory();

}
