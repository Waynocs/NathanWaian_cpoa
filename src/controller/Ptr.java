package controller;

public class Ptr<T> {
    public T value;

    public Ptr(T val) {
        value = val;
    }

    public Ptr() {
        this(null);
    }
}