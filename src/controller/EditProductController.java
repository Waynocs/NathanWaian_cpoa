package controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Category;
import model.Product;

public class EditProductController implements Initializable {
    @FXML
    public ComboBox<Category> category;
    @FXML
    public Hyperlink categLink;
    @FXML
    public Label id;
    @FXML
    public TextField name;
    @FXML
    public TextArea description;
    @FXML
    public Spinner<Double> cost;
    @FXML
    public TextField image;
    @FXML
    public Tab tab;
    public Product product;

    public static Tab createControl(Product prod) {
        try {
            URL fxmlURL = EditProductController.class.getResource("../view/EditProduct.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            var control = fxmlLoader.<TabPane>load();
            var controller = fxmlLoader.<EditProductController>getController();
            controller.setupFields(prod);
            return control.getTabs().get(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    public void setupFields(Product prod) {
        product = prod;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    }

    public void refresh() {
    }

    public void edit() {
        // TODO open a edit product tab
    }
}