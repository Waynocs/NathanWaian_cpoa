package controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Customer;

public class CustomersController implements Initializable {

    @FXML
    public TableView<Customer> table;
    @FXML
    public TableColumn<Customer, String> id;
    @FXML
    public TableColumn<Customer, String> name;
    @FXML
    public TableColumn<Customer, String> surname;
    @FXML
    public TableColumn<Customer, String> identity;
    @FXML
    public TableColumn<Customer, String> password;
    @FXML
    public TableColumn<Customer, String> number;
    @FXML
    public TableColumn<Customer, String> street;
    @FXML
    public TableColumn<Customer, String> postalcode;
    @FXML
    public TableColumn<Customer, String> city;
    @FXML
    public TableColumn<Customer, String> country;
    @FXML
    public TableColumn<Customer, Void> detail;
    @FXML
    public TableColumn<Customer, Void> remove;
    @FXML
    public TextField searchbar;

    public static Tab createControl() {
        try {
            URL fxmlURL = CustomersController.class.getResource("../view/Customers.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            return fxmlLoader.<TabPane>load().getTabs().get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

}