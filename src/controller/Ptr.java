package controller;

/**
 * Class mostly used to store a variable used inside multiple lamda methods
 */
public class Ptr<T> {
    public T value;

    public Ptr(T val) {
        value = val;
    }

    public Ptr() {
        this(null);
    }
}