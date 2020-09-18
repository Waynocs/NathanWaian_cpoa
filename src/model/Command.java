package model;

import java.util.*;
import java.time.*;

public class Command {

    private int id;
    private LocalDate date;
    private Customer customer;
    private Map<Integer, Line> products;

    private class Line {

        public Product product;
        public double cost;
        public int quantity;

    }
}
