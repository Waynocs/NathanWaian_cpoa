package model;

import java.util.Objects;

/**
 * A line for a product in an order
 */
public class OrderLine implements Base<OrderLine> {

    @Override
    public OrderLine clone() {
        return new OrderLine(order, product, cost, quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderLine)) {
            return false;
        }
        OrderLine orderLine = (OrderLine) o;
        return order == orderLine.order && product == orderLine.product;
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, product);
    }

    /**
     * Returns the order for this line
     * 
     * @return the order for this line
     */
    public int getOrder() {
        return this.order;
    }

    /**
     * Returns the product for this line
     * 
     * @return the product for this line
     */
    public int getProduct() {
        return this.product;
    }

    /**
     * Returns the unit cost of the product
     * 
     * @return
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Changes the unit cost of the product
     * 
     * @param cost new unit cost of the product
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Changes the quantity of the product
     * 
     * @return the quantity of the product
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Changes the quantity of the product
     * 
     * @param quantity the new quantity of the product
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private int order;
    private int product;
    private double cost;
    private int quantity;

    /**
     * Constructor.
     * 
     * @param order    the order of the line
     * @param product  the product of the line
     * @param cost     the unit cost of the product
     * @param quantity the quantity of the product
     */
    public OrderLine(int order, int product, double cost, int quantity) {
        this.order = order;
        this.product = product;
        this.cost = cost;
        this.quantity = quantity;
    }
}
