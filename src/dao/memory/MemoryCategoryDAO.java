package dao.memory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import dao.DAOException;
import model.Category;
import model.Product;

/**
 * Class used to manage categories using the MemoryDAOFactory
 */
public class MemoryCategoryDAO implements dao.CategoryDAO {
    private static int index = 0;

    private class Data {
        public String name;
        public String imgPath;

        public Data(String n, String iP) {
            name = n;
            imgPath = iP;
        }
    }

    private static MemoryCategoryDAO instance;

    private Map<Integer, Data> memory;

    private MemoryCategoryDAO() {
        memory = new HashMap<Integer, Data>();
    }

    /**
     * Returns the only instance of this class
     * 
     * @return the only instance of this class
     */
    public static MemoryCategoryDAO getInstance() {
        return instance == null ? instance = new MemoryCategoryDAO() : instance;
    }

    @Override
    public Category create(Category object) {
        memory.put(index++, new Data(object.getName(), object.getImagePath()));
        return getById(index - 1);
    }

    @Override
    public boolean update(Category object) {
        if (!memory.keySet().contains(object.getId()))
            return false;
        else {
            memory.put(object.getId(), new Data(object.getName(), object.getImagePath()));
            return true;
        }
    }

    @Override
    public boolean delete(Category object) {
        for (Product product : MemoryProductDAO.getInstance().getAll())
            if (product.getCategory() == object.getId())
                throw new DAOException("The category '" + object.getId() + "' is used by a product");
        if (!memory.keySet().contains(object.getId()))
            return false;
        else {
            memory.remove(object.getId());
            return true;
        }
    }

    @Override
    public Category getById(int id) {
        if (!memory.keySet().contains(id))
            return null;
        else {
            var element = memory.get(id);
            return new Category(element.name, element.imgPath, id);
        }
    }

    @Override
    public Category[] getAll() {
        var list = new LinkedList<Category>();
        for (var entry : memory.entrySet())
            list.add(new Category(entry.getValue().name, entry.getValue().imgPath, entry.getKey()));
        return list.toArray(new Category[0]);
    }

}
