package controller;

import java.io.IOException;
import java.lang.invoke.ClassSpecializer.Factory;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import model.Product;

public class ProductsController implements Initializable {
    @FXML
    public TreeTableView<Product> table;
    @FXML
    public TreeTableColumn<Product, Void> category;
    @FXML
    public TreeTableColumn<Product, Integer> id;
    @FXML
    public TreeTableColumn<Product, String> name;
    @FXML
    public TreeTableColumn<Product, String> description;
    @FXML
    public TreeTableColumn<Product, String> price;
    @FXML
    public TreeTableColumn<Product, String> image;
    @FXML
    public TreeTableColumn<Product, Void> detail;
    @FXML
    public TreeTableColumn<Product, Void> remove;

    public static Tab createControl() {
        try {
            URL fxmlURL = ProductsController.class.getResource("../Products.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            return ((TabPane) fxmlLoader.load()).getTabs().get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        table.setRoot(new TreeItem<Product>());
        category.setCellFactory(new Callback<TreeTableColumn<Product, Void>, TreeTableCell<Product, Void>>() {
            @Override
            public TreeTableCell<Product, Void> call(TreeTableColumn<Product, Void> arg0) {
                return new TreeTableCell<Product, Void>() {

                    private Hyperlink link = new Hyperlink(MainWindowController.factory.getCategoryDAO()
                            .getById(getTreeTableView().getRoot().getValue().getCategory()).getName());

                    {
                        link.setOnAction((ActionEvent event) -> {

                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(link);
                        }
                    }
                };
            };
        });

    }
}