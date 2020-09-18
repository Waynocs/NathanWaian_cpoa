package model;

/**
 * Defines a category
 */
public class Category {
    private String name;
    private String imagePath;
    private int id;
    /**
     * Constructor
     * @param name the name of the category
     * @param imagePath the path to the image of this category
     * @param id the id of this category
     */
    public Category(String name, String imagePath, int id) {
        this.setName(name);
        this.setImagePath(imagePath);
        this.setId(id);
    }
    /**
     * Returns the id of the category
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
     * @return the path to the image of the category
     */
    public String getImagePath() {
        return imagePath;
    }
    /**
     * Changes the path to
     * @param imagePath
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
