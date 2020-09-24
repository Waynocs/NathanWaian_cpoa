package model;

import java.util.*;
import java.time.*;

public class Command {

    private int id;
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
