package dao.Memory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import model.Product;

public class MemoryProductDAO implements dao.ProductDAO {

    private class Data {
        public String name;
        public String imgPath;
        public double cost;
        public String description;
        public int category;

        public Data(String n, String iP, double cost) {
            name = n;
            imgPath = iP;

        }
    }

    private static MemoryProductDAO instance;

    private Map<Integer, Data> memory;

    private MemoryProductDAO() {
        memory = new HashMap<Integer, Data>();
    }

    /**
     * Returns the only instance of this class
     * 
     * @return the only instance of this class
     */
    public static MemoryProductDAO getInstance() {
        return instance == null ? instance = new MemoryProductDAO() : instance;
    }

    /**
     * create a new product
     * 
     * @param id
     * @param name
     * @param imagePath
     * @param cost
     */

    @Override
    public boolean create(Product object) throws SQLException {
        if (memory.keySet().contains(object.getId()))
            return false;
        else {
            memory.put(object.getId(), new Data(object.getName(), object.getImagePath(), object.getCost()));
            return true;
        }
    }

    /**
     * update a product
     * 
     * @param id
     * @param name
     * @param imagePath
     * @param cost
     */

    @Override
    public boolean update(Product object) throws SQLException {
        if (!memory.keySet().contains(object.getId()))
            return false;
        else {
            memory.put(object.getId(), new Data(object.getName(), object.getImagePath(), object.getCost()));
            return true;
        }
    }

    /**
     * delete a product
     * 
     * @param id
     */

    @Override
    public boolean delete(Product object) throws SQLException {
        if (!memory.keySet().contains(object.getId()))
            return false;
        else {
            memory.remove(object.getId());
            return true;
        }
    }

    @Override
    public Product getById(int id) throws SQLException {

        if (!memory.keySet().contains(id)) {
            return null;
        } else {
            var element = memory.get(id);
            return new Product(id, element.name, element.cost, element.imgPath, element.category, element.description);
        }
    }

    /**
     * show all product
     * 
     * @param id
     * @param name
     * @param imagePath
     * @param cost
     * @param description
     * @param category
     */

    @Override
    public Product[] getAll() throws SQLException {

        var list = new LinkedList<Product>();
        for (var entry : memory.entrySet())
            list.add(new Product(entry.getKey(), entry.getValue().imgPath, entry.getValue().cost,
                    entry.getValue().description, entry.getValue().category, entry.getValue().name));
        return list.toArray(new Product[0]);

    }

}
