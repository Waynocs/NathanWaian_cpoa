package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.DAOFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainWindowController implements Initializable {
    public static Stage window;

    public static final Image detailImage = new Image("assets/icons/detail.png");
    public static final Image removeImage = new Image("assets/icons/remove.png");
    @FXML
    public TabPane mainTabPane;
    private static TabPane tabInstance;
    private static MainWindowController mainInstance;

    public static MainWindowController getInstance() {
        return mainInstance;
    }

    public static TabPane getMainTabPane() {
        return tabInstance;
    }

    public void close() {
        window.close();
    }

    public static DAOFactory factory;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        tabInstance = mainTabPane;

    }

    public void addCategory() {

    }

    public void addCustomer() {

    }

    public void addProduct() {

    }

    public void addOrder() {

    }

    public void seeCategories() {

    }

    public void seeCustomers() {

    }

    public void seeProducts() {
        var tab = ProductsController.createControl();
        mainTabPane.getTabs().add(tab);
        mainTabPane.getSelectionModel().select(tab);
    }

    public void seeOrders() {

    }

    public void detailCategory(Integer id) {

    }

    public void detailCustomer(Integer id) {

    }

    public void detailProduct(Integer id) {

    }

    public void detailOrder(Integer id) {

    }
}
