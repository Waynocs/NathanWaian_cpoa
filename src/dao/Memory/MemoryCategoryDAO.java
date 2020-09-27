package dao.Memory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import model.Category;

/**
 * Class used to manage categories using the MemoryDAOFactory
 */
public class MemoryCategoryDAO implements dao.CategoryDAO {

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

    /**
     * create a new category
     * 
     * @param id
     * @param name
     * @param imagePath
     */

    @Override
    public boolean create(Category object) throws SQLException {
        int id = -1;
        for (var key : memory.keySet())
            if (key > id)
                id = key;
        memory.put(id + 1, new Data(object.getName(), object.getImagePath()));
        return true;
    }

    /**
     * update a category
     * 
     * @param id
     * @param name
     * @param imagePath
     */

    @Override
    public boolean update(Category object) throws SQLException {
        if (!memory.keySet().contains(object.getId()))
            return false;
        else {
            memory.put(object.getId(), new Data(object.getName(), object.getImagePath()));
            return true;
        }
    }

    /**
     * delete a category
     * 
     * @param id
     */

    @Override
    public boolean delete(Category object) throws SQLException {
        if (!memory.keySet().contains(object.getId()))
            return false;
        else {
            memory.remove(object.getId());
            return true;
        }
    }

    @Override
    public Category getById(int id) throws SQLException {
        if (!memory.keySet().contains(id))
            return null;
        else {
            var element = memory.get(id);
            return new Category(element.name, element.imgPath, id);
        }
    }

    /**
     * show all category
     * 
     * @param id
     * @param name
     * @param imagePath
     */

    @Override
    public Category[] getAll() throws SQLException {
        var list = new LinkedList<Category>();
        for (var entry : memory.entrySet())
            list.add(new Category(entry.getValue().name, entry.getValue().imgPath, entry.getKey()));
        return list.toArray(new Category[0]);
    }

}
