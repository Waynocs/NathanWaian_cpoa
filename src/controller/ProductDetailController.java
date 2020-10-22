package controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.application.Platform;
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
import javafx.scene.control.TextArea;
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
    public Tab tab;
    public Product product;

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
        tab.setText("Détail:" + product.getName());
        category.setText(MainWindowController.factory.getCategoryDAO().getById(product.getCategory()).getName());
        category.setOnAction((ActionEvent e) -> {
            // TODO open a category detail tab
        });
        name.setText("Nom : " + product.getName());
        id.setText("ID : " + product.getId());
        description.setText(product.getDescription());
        price.setText("Prix : " + product.getCost() + " €");
        image.setText("Visuel : " + product.getImagePath());
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    }

    public void refresh() {
        MainWindowController.runAsynchronously(new Runnable() {
            @Override
            public void run() {
                product = MainWindowController.factory.getProductDAO().getById(product.getId());
                var categ = MainWindowController.factory.getCategoryDAO().getById(product.getCategory());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tab.setText("Détail:" + product.getName());
                        category.setText(categ.getName());
                        name.setText("Nom : " + product.getName());
                        id.setText("ID : " + product.getId());
                        description.setText(product.getDescription());
                        price.setText("Prix : " + product.getCost() + " €");
                        image.setText("Visuel : " + product.getImagePath());
                    }
                });
            }
        });
    }

    public void edit() {
        MainWindowController.editProduct(product, this);
    }

    public void remove() {
        MainWindowController.removeProduct(product, new Runnable() {

            @Override
            public void run() {
                EventHandler<Event> handler = tab.getOnClosed();
                if (null != handler) {
                    handler.handle(null);
                } else {
                    tab.getTabPane().getTabs().remove(tab);
                }
            }

        }, null);
    }
}