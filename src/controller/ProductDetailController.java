package controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import model.Category;
import model.OrderLine;
import model.Product;

public class ProductDetailController implements Initializable {
    @FXML
    public Hyperlink category;
    @FXML
    public Label id;
    @FXML
    public Label name;
    @FXML
    public TextArea description;
    @FXML
    public Label price;
    @FXML
    public Label image;
    @FXML
    public Label ordered;
    @FXML
    public Tab tab;
    public Product product;
    public Category categ;

    public static Tab createControl(Product prod) {
        try {
            URL fxmlURL = ProductDetailController.class.getResource("../view/ProductDetail.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            var control = fxmlLoader.<TabPane>load();
            var controller = fxmlLoader.<ProductDetailController>getController();
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
        refresh();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    }

    public void refresh() {
        MainWindowController.runAsynchronously(() -> {
            product = MainWindowController.factory.getProductDAO().getById(product.getId());
            categ = MainWindowController.factory.getCategoryDAO().getById(product.getCategory());
            return MainWindowController.factory.getOrderLineDAO().getAllFromProduct(product.getId());
        }, (orders) -> {
            tab.setText("Détail:" + product.getName());
            category.setText(categ.getName());
            category.setOnAction((e) -> MainWindowController.detailCategory(categ));
            name.setText("Nom : " + product.getName());
            id.setText("ID : " + product.getId());
            description.setText(product.getDescription());
            price.setText("Prix : " + product.getCost() + " €");
            image.setText("Visuel : " + product.getImagePath());
            int count = 0;
            for (var orderLine : orders)
                count += orderLine.getQuantity();
            ordered.setText("Commandés : " + count);
        });
    }

    public void edit() {
        MainWindowController.editProduct(product, this);
    }

    public void remove() {
        MainWindowController.removeProduct(product, () -> {
            EventHandler<Event> handler = tab.getOnClosed();
            if (null != handler) {
                handler.handle(null);
            } else {
                tab.getTabPane().getTabs().remove(tab);
            }
        }, null);
    }
}