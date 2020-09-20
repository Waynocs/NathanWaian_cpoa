package model;

import java.util.*;
import java.time.*;

public class Command {

    private int id;

    /**
     * 
     * @param id
     * @param date
     * @param customer
     */
    public Command(int id, LocalDate date, Customer customer) {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.products = new HashMap<Integer, Line>();
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public int getId() {
        return this.id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    private LocalDate date;
    private Customer customer;
    private Map<Integer, Line> products;

    private class Line {

        public Product product;
        public double cost;
        public int quantity;

        public Line(Product product, double cost, int quantity) throws Exception {
            this.product = product;
            this.cost = cost;
            this.quantity = quantity;

            
            if(products.containsKey(product.getId())) {
                throw new Exception("The product already exists on command with product id: " + product.getId());
            } else {
               
                products.put(product.getId(), new Line(product, cost, quantity));
            }


    }
}
}
