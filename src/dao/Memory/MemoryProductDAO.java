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

        public Data(String n, String iP) {
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

    @Override
    public boolean create(Product object) throws SQLException {
        if (memory.keySet().contains(object.getId()))
            return false;
        else {
            memory.put(object.getId(), new Data(object.getName(), object.getImagePath()));
            return true;
        }
    }

    @Override
    public boolean update(Product object) throws SQLException {
        if (!memory.keySet().contains(object.getId()))
            return false;
        else {
            memory.put(object.getId(), new Data(object.getName(), object.getImagePath()));
            return true;
        }
    }

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
        /*
         * if (!memory.keySet().contains(id)) return null; else { var element =
         * memory.get(id); return new Product(id, element.name, cost, element.imgPath,
         * element.description, category);
         */
        return null;
    }

    @Override
    public Product[] getAll() throws SQLException {
        /*
         * var list = new LinkedList<Product>(); for (var entry : memory.entrySet())
         * list.add(new Product(entry.getKey().id, entry.getValue().name,
         * cost.getKey())); return list.toArray(new Product[0]);
         */
        return null;
    }

}
