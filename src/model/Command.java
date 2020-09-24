package model;

import java.util.*;

import java.time.*;

/**
 * 
 */
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

    public void addProduct(Product p, int count) throws InvalidProductException {
        if (count < 1)
            throw new InvalidProductException("'count' must over 0");
        else if (products.keySet().contains(p.getId()))
            throw new InvalidProductException("The item " + p.getName() + " is already added");
        else
            products.put(p.getId(), new Line(p, count));
    }

    public void addProduct(Product p, int count, double cost) throws InvalidProductException {
        if (count < 1)
            throw new InvalidProductException("'count' must over 0");
        else if (products.keySet().contains(p.getId()))
            throw new InvalidProductException("The item " + p.getName() + " is already added");
        else
            products.put(p.getId(), new Line(p, count, cost));
    }

    private LocalDate date;
    private Customer customer;
    private Map<Integer, Line> products;

    private class Line {

        public Product product;
        public double cost;
        public int quantity;

        public Line(Product product, int quantity, double cost) {
            this.setProduct(product);
            this.setCost(cost);
            this.setQuantity(quantity);
        }

        public Line(Product product, int quantity) {
            this.setProduct(product);
            this.setCost(product.getCost());
            this.setQuantity(quantity);
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            if (product != null)
                this.product = product;
        }

        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            if (cost > 0)
                this.cost = cost;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            if (quantity > 0)
                this.quantity = quantity;

        }

    }

}
