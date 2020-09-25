package model;

/**
 * Defines a customer
 */
public class Customer {
    private int id;
    private String name;
    private String surname;
    private String identifier;
    private String pwd;
    private String addressNumber;
    private String addressStreet;
    private String addressPostalCode;
    private String addressCity;
    private String addressCountry;

    /**
     * Returns the identifier of the customer
     * 
     * @return the identifier of the customer
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Changes the identifier of the customer
     * 
     * @param identifier the new identifier of the customer
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Returns the password of the customer
     * 
     * @return the password of the customer
     */
    public String getPwd() {
        return this.pwd;
    }

    /**
     * Changes the password of the customer
     * 
     * @param pwd the new password of the customer
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * Returns the address number of the customer
     * 
     * @return the address number of the customer
     */
    public String getAddressNumber() {
        return this.addressNumber;
    }

    /**
     * Changes the address number of the customer
     * 
     * @param addressNumber the new address number of the customer
     */
    public void setAddressNumber(String addressNumber) {
        this.addressNumber = addressNumber;
    }

    /**
     * Returns the address street of the customer
     * 
     * @return the address street of the customer
     */
    public String getAddressStreet() {
        return this.addressStreet;
    }

    /**
     * Changes the adress street of the customer
     * 
     * @param addressStreet the new adress street of the customer
     */
    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    /**
     * Returns the postal code of the customer
     * 
     * @return the postal code of the customer
     */
    public String getAddressPostalCode() {
        return this.addressPostalCode;
    }

    /**
     * Constructor
     * 
     * @param id                the id of the customer. Useless when creating a
     *                          customer
     * @param name              the name of the customer
     * @param surname           the surname of the customer
     * @param identifier        the identifier of the customer
     * @param pwd               the password of the customer
     * @param addressNumber     the address number of the customer
     * @param addressStreet     the address street of the customer
     * @param addressPostalCode the postal code of the customer
     * @param addressCity       the city of the customer
     * @param addressCountry    the country of the customer
     */
    public Customer(int id, String name, String surname, String identifier, String pwd, String addressNumber,
            String addressStreet, String addressPostalCode, String addressCity, String addressCountry) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.identifier = identifier;
        this.pwd = pwd;
        this.addressNumber = addressNumber;
        this.addressStreet = addressStreet;
        this.addressPostalCode = addressPostalCode;
        this.addressCity = addressCity;
        this.addressCountry = addressCountry;
    }

    /**
     * Changes the postal code of the customer
     * 
     * @param addressPostalCode thte new postal code of the customer
     */
    public void setAddressPostalCode(String addressPostalCode) {
        this.addressPostalCode = addressPostalCode;
    }

    /**
     * Returns the city of the customer
     * 
     * @return the city of the customer
     */
    public String getAddressCity() {
        return this.addressCity;
    }

    /**
     * Changes the city of the customer
     * 
     * @param addressCity the new city of the customer
     */
    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    /**
     * Returns the country of the customer
     * 
     * @return the country of the customer
     */
    public String getAddressCountry() {
        return this.addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    /**
     * Returns the surname of this customer
     * 
     * @return the surname of this customer
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Changes the surname of the customer
     * 
     * @param surname the new surname of the customer
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Returns the id of the customer
     * 
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
     * 
     * @return the name of the customer
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of this customer
     * 
     * @param name the new name of this customer
     */
    public void setName(String name) {
        this.name = name;
    }
}
