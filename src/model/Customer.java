package src.model;

/**
 * Defines a customer
 */
public class Customer {
    private int id;
    private String name;
    private String surname;
    /**
     * Constructor
     * @param id id of the customer
     * @param name name of the customer
     * @param surname surname of the customer
     */
    public Customer(int id, String name, String surname) {
        this.setId(id);
        this.setName(name);
        this.setSurname(surname);
    }
    /**
     * Returns the surname of this customer
     * @return the surname of this customer
     */
    public String getSurname() {
        return surname;
    }
    /**
     * Changes the surname of the customer
     * @param surname the new surname of the customer
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
    /**
     * Returns the id of the customer
     * @return the id of the customer
     */
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }
    /**
     * Returns the name of the customer
     * @return the name of the customer
     */
    public String getName() {
        return name;
    }
    /**
     * Changes the name of this customer
     * @param name the new name of this customer
     */
    public void setName(String name) {
        this.name = name;
    }
}
