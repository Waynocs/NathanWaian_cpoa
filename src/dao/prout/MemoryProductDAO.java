package dao.memory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import model.Product;

/**
 * Class used to manage products using the MySQLDAOFactory
 */
public class MemoryProductDAO implements dao.ProductDAO {
    private static int index = 0;

    private class Data {
        public String name;
        public String imgPath;
        public double cost;
        public String description;
        public int category;

        public Data(String name, String imgPath, double cost, String description, int category) {
            this.name = name;
            this.imgPath = imgPath;
            this.cost = cost;
            this.description = description;
            this.category = category;
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
    public boolean create(Product object) {
        memory.put(index++, new Data(object.getName(), object.getImagePath(), object.getCost(), object.getDescription(),
                object.getCategory()));
        return true;
    }

    @Override
    public boolean update(Product object) {
        if (!memory.keySet().contains(object.getId()))
            return false;
        else {
            memory.put(object.getId(), new Data(object.getName(), object.getImagePath(), object.getCost(),
                    object.getDescription(), object.getCategory()));
            return true;
        }
    }

    @Override
    public boolean delete(Product object) {
        if (!memory.keySet().contains(object.getId()))
            return false;
        else {
            memory.remove(object.getId());
            return true;
        }
    }

    @Override
    public Product getById(int id) {

        if (!memory.keySet().contains(id)) {
            return null;
        } else {
            var element = memory.get(id);
            return new Product(id, element.name, element.cost, element.description, element.category, element.imgPath);
        }
    }

    @Override
    public Product[] getAll() {

        var list = new LinkedList<Product>();
        for (var entry : memory.entrySet())
            list.add(new Product(entry.getKey(), entry.getValue().name, entry.getValue().cost,
                    entry.getValue().description, entry.getValue().category, entry.getValue().imgPath));
        return list.toArray(new Product[0]);

    }

}
