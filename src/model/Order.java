package model;

import java.time.*;
import java.util.Objects;

/**
 * Defines an order made by a customer
 */
public class Order implements Base<Order> {

    private int id;
    private LocalDateTime date;
    private int customer;

    @Override
    public Order clone() {
        return new Order(id, date, customer);
    }

    /**
     * Constructor.
     * 
     * @param id         id of the order
     * @param date       the date the order has been made
     * @param customerID the customer who made the order
     */
    public Order(int id, LocalDateTime date, int customerID) {
        this.id = id;
        this.date = date;
        this.customer = customerID;
    }

    /**
     * Returns the date of the order
     * 
     * @return the date of the order
     */
    public LocalDateTime getDate() {
        return this.date;
    }

    /**
     * Changes the date of the order
     * 
     * @param date the new date of the order
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Returns the customer who made the order
     * 
     * @return the customer who made the order
     */
    public int getCustomer() {
        return this.customer;
    }

    /**
     * Returns the id of the order
     * 
     * @return the id of the order
     */
    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Order)) {
            return false;
        }
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * Changes the customer who made the order
     * 
     * @param customer the new customer who made the order
     */
    public void setCustomer(int customerID) {
        this.customer = customerID;
    }

}
