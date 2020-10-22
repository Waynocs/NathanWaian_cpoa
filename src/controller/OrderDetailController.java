package controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Order;
import model.OrderLine;
import model.Product;

public class OrderDetailController implements Initializable {
    @FXML
    public Label id;
    @FXML
    public Hyperlink customer;
    @FXML
    public Label date;
    @FXML
    public Label time;
    @FXML
    public TableView<OrderLine> table;
    @FXML
    public TableColumn<OrderLine, String> products;
    @FXML
    public TableColumn<OrderLine, String> quantities;
    @FXML
    public TableColumn<OrderLine, String> prices;
    @FXML
    public TableColumn<OrderLine, String> totals;
    @FXML
    public Label totalCost;
    @FXML
    public Tab tab;
    public Order order;
    public ObservableList<OrderLine> displayedLines;

    public static Tab createControl(Order ord) {
        try {
            URL fxmlURL = ProductDetailController.class.getResource("../view/OrderDetail.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            var control = fxmlLoader.<TabPane>load();
            var controller = fxmlLoader.<OrderDetailController>getController();
            controller.setupFields(ord);
            return control.getTabs().get(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    public void setupFields(Order ord) {

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        displayedLines = FXCollections.observableList(new ArrayList<OrderLine>());
    }

    public void refresh() {

    }

    public void edit() {

    }

    public void remove() {

    }
}
