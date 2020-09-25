package model;

import java.time.*;

/**
 * Defines an order made by a customer
 */
public class Order {

    private int id;
    private LocalDateTime date;
    private int customer;

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

    /**
     * Changes the customer who made the order
     * 
     * @param customer the new customer who made the order
     */
    public void setCustomer(int customerID) {
        this.customer = customerID;
    }

}
