package model;
/**
 * Defines a product
 */
public class Product {
    private int id;
    private String name;
    private double cost;
    private String description;
    private Category category;
    private String imagePath;
    /**
     * Returns the name of the product
     * @return the name of the product
     */
    public String getName() {
        return this.name;
    }
    /**
     * Changes the name of the product
     * @param name the name of the product
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Constructor
     * @param id hthe id of the product
     * @param name the name of the product
     * @param cost the cost of the product
     * @param description the description of the product
     * @param category the category of the product
     * @param imagePath the image file path of the product
     */
    public Product(int id, String name, double cost, String description, Category category, String imagePath) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.description = description;
        this.category = category;
        this.imagePath = imagePath;
    }
    /**
     * Returns the cost of the product
     * @return the cost of the product
     */
    public double getCost() {
        return this.cost;
    }
    /**
     * Changes the cost of the product
     * @param cost the new cost of the product
     */
    public void setCost(double cost) {
        this.cost = cost;
    }
    /**
     * Returns the description of the product
     * @return the description of the product
     */
    public String getDescription() {
        return this.description;

    }
    /**
     * Returns the category of the product
     * @return the category of the product
     */
    public Category getCategory() {
        return this.category;
    }
    /**
     * Changes the description of the product
     * @param description the new description of the product
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Returns the image file path of the product
     * @return the image file path of the product
     */
    public String getImagePath() {
        return this.imagePath;
    }
    /**
     * Changes the image file path of the product
     * @param imagePath the image file path of the product
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    /**
     * Returns the id of the product
     * @return the id of the product
     */
    public int getId() {
        return this.id;
    }
}
