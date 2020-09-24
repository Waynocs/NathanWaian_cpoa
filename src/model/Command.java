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

        public int idProduct;
        public double cost;
        public int quantity;

        public Line(int idProduct, double cost, int quantity) {
            this.setIdProduct(idProduct);
            this.setCost(cost);
            this.setQuantity(quantity);
        }

        public int getIdProduct() {
            return idProduct;
        }

        public void setIdProduct(int idProduct) {
            if (idProduct > 0)
                this.idProduct = idProduct;
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
