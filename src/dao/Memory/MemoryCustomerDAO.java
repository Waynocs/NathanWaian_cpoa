package dao.Memory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import model.Customer;

public class MemoryCustomerDAO implements dao.CustomerDAO {

    private class Data {
        public String name;
        public String surname;
        public String identifier;
        public String pwd;
        public String addressNumber;
        public String addressStreet;
        public String addressPostalCode;
        public String addressCity;
        public String addressCountry;

        public Data(final String n, final String surname, final String identifier, final String pwd,
                final String adressNumber, final String adressStreet, final String adressPostalCode,
                final String adressCity, final String adressCountry) {

        }
    }

    private static MemoryCustomerDAO instance;

    private final Map<Integer, Data> memory;

    private MemoryCustomerDAO() {
        memory = new HashMap<Integer, Data>();
    }

    /**
     * Returns the only instance of this class
     * 
     * @return the only instance of this class
     */
    public static MemoryCustomerDAO getInstance() {
        return instance == null ? instance = new MemoryCustomerDAO() : instance;
    }

    /**
     * create a new customer
     * 
     * @param name
     * @param surname
     * @param identifier
     * @param pwd
     * @param adressStreet
     * @param adressNumber
     * @param adressPostalCode
     * @param adressCity
     * @param adressCountry
     */

    @Override
    public boolean create(final Customer object) throws SQLException {
        int id = -1;
        for (var key : memory.keySet())
            if (key > id)
                id = key;
        memory.put(id + 1,
                new Data(object.getName(), object.getSurname(), object.getIdentifier(), object.getPwd(),
                        object.getAddressNumber(), object.getAddressStreet(), object.getAddressPostalCode(),
                        object.getAddressCity(), object.getAddressCountry()));
        return true;
    }

    /**
     * update a customer
     * 
     * @param id
     * @param name
     * @param surname
     * @param identifier
     * @param pwd
     * @param adressStreet
     * @param adressNumber
     * @param adressPostalCode
     * @param adressCity
     * @param adressCountry
     */

    @Override
    public boolean update(final Customer object) throws SQLException {
        if (!memory.keySet().contains(object.getId()))
            return false;
        else {
            memory.put(object.getId(),
                    new Data(object.getName(), object.getSurname(), object.getIdentifier(), object.getPwd(),
                            object.getAddressNumber(), object.getAddressStreet(), object.getAddressPostalCode(),
                            object.getAddressCity(), object.getAddressCountry()));
            return true;
        }
    }

    /**
     * delete a customer
     * 
     * @param id
     */

    @Override
    public boolean delete(final Customer object) throws SQLException {
        if (!memory.keySet().contains(object.getId()))
            return false;
        else {
            memory.remove(object.getId());
            return true;
        }
    }

    @Override
    public Customer getById(final int id) throws SQLException {
        if (!memory.keySet().contains(id))
            return null;
        else {
            var element = memory.get(id);
            return new Customer(id, element.name, element.surname, element.identifier, element.pwd,
                    element.addressStreet, element.addressNumber, element.addressPostalCode, element.addressCity,
                    element.addressCountry);
        }
    }

    /**
     * show all customer
     * 
     * @param name
     * @param surname
     * @param identifier
     * @param pwd
     * @param adressStreet
     * @param adressNumber
     * @param adressPostalCode
     * @param adressCity
     * @param adressCountry
     */

    @Override
    public Customer[] getAll() throws SQLException {
        var list = new LinkedList<Customer>();
        for (var entry : memory.entrySet())
            list.add(new Customer(entry.getKey(), entry.getValue().name, entry.getValue().surname,
                    entry.getValue().identifier, entry.getValue().pwd, entry.getValue().addressStreet,
                    entry.getValue().addressNumber, entry.getValue().addressPostalCode, entry.getValue().addressCity,
                    entry.getValue().addressCountry));
        return list.toArray(new Customer[0]);
    }
}
