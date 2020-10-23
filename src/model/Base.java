package model;

/**
 * Base interface of all models
 */
public interface Base<T> extends Cloneable {
    T clone();
}
