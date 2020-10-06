package model;

import java.util.Objects;

/**
 * Defines a category
 */
public class Category {
    private String name;
    private String imagePath;
    private int id;

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Category)) {
            return false;
        }
        Category category = (Category) o;
        return id == category.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * Constructor
     * 
     * @param name      the name of the category
     * @param imagePath the path to the image of this category
     * @param id        the id of this category
     */
    public Category(String name, String imagePath, int id) {
        this.setName(name);
        this.setImagePath(imagePath);
        this.setId(id);
    }

    /**
     * Returns the id of the category
     * 
     * @return the id of the category
     */
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the path to the image of the category
     * 
     * @return the path to the image of the category
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Changes the path to the image of the category
     * 
     * @param imagePath the new path to the image of the category
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Returns the name of the category
     * 
     * @return the name of the category
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the category
     * 
     * @param name the new name of the category
     */
    public void setName(String name) {
        this.name = name;
    }
}
