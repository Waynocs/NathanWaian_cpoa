package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.DAOFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;

public class MainWindowController implements Initializable {

    @FXML
    public TabPane mainTabPane;
    private static TabPane tabInstance;

    public static TabPane getMainTabPane() {
        return tabInstance;
    }

    public static DAOFactory factory;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        tabInstance = mainTabPane;
        mainTabPane.getTabs().add(ProductsController.createControl());
    }

    public static void addCategory() {

    }

    public static void addCustomer() {

    }

    public static void addProduct() {

    }

    public static void addOrder() {

    }

    public static void seeCategories() {

    }

    public static void seeCustomers() {

    }

    public static void seeProducts() {

    }

    public static void seeOrders() {

    }

    public static void detailCategory(Integer id) {

    }

    public static void detailCustomer(Integer id) {

    }

    public static void detailProduct(Integer id) {

    }

    public static void detailOrder(Integer id) {

    }
}
