package dao.memory;

import java.util.*;

import dao.OrderLineDAO;
import model.OrderLine;

/**
 * Class used to manage order lines using the MySQLDAOFactory
 */
public class MemoryOrderLineDAO implements OrderLineDAO {

    private class DoubleIntID {
        public int id1;
        public int id2;

        public DoubleIntID(int i1, int i2) {
            id1 = i1;
            id2 = i2;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this)
                return true;
            else if (other instanceof DoubleIntID)
                return ((DoubleIntID) other).id1 == id1 && ((DoubleIntID) other).id2 == id2;
            else
                return false;
        }
    }

    private class Data {
        public double cost;
        public int quantity;

        public Data(double cost, int quantity) {
            this.cost = cost;
            this.quantity = quantity;
        }
    }

    private static MemoryOrderLineDAO instance;

    private Map<DoubleIntID, Data> memory;

    private MemoryOrderLineDAO() {
        memory = new HashMap<DoubleIntID, Data>();
    }

    /**
     * Returns the only instance of this class
     * 
     * @return the only instance of this class
     */
    public static MemoryOrderLineDAO getInstance() {
        return instance == null ? instance = new MemoryOrderLineDAO() : instance;
    }

    @Override
    public boolean create(OrderLine object) {
        if (memory.keySet().contains(new DoubleIntID(object.getOrder(), object.getProduct())))
            return false;
        memory.put(new DoubleIntID(object.getOrder(), object.getProduct()),
                new Data(object.getCost(), object.getQuantity()));
        return true;
    }

    @Override
    public boolean update(OrderLine object) {
        if (!memory.keySet().contains(new DoubleIntID(object.getOrder(), object.getProduct())))
            return false;
        memory.put(new DoubleIntID(object.getOrder(), object.getProduct()),
                new Data(object.getCost(), object.getQuantity()));
        return true;
    }

    @Override
    public boolean delete(OrderLine object) {
        if (!memory.keySet().contains(new DoubleIntID(object.getOrder(), object.getProduct())))
            return false;
        memory.remove(new DoubleIntID(object.getOrder(), object.getProduct()));
        return true;
    }

    @Override
    public OrderLine getById(int order, int product) {
        var id = new DoubleIntID(order, product);
        var data = memory.keySet().contains(id) ? memory.get(id) : null;
        return data == null ? null : new OrderLine(order, product, data.cost, data.quantity);
    }

    @Override
    public OrderLine[] getAllFromOrder(int order) {
        var list = new LinkedList<OrderLine>();
        for (var entry : memory.entrySet())
            if (entry.getKey().id1 == order)
                list.add(new OrderLine(entry.getKey().id1, entry.getKey().id2, entry.getValue().cost,
                        entry.getValue().quantity));
        return list.toArray(new OrderLine[0]);
    }

    @Override
    public OrderLine[] getAllFromProduct(int product) {
        var list = new LinkedList<OrderLine>();
        for (var entry : memory.entrySet())
            if (entry.getKey().id2 == product)
                list.add(new OrderLine(entry.getKey().id1, entry.getKey().id2, entry.getValue().cost,
                        entry.getValue().quantity));
        return list.toArray(new OrderLine[0]);
    }
}
