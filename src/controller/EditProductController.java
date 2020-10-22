package controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.util.converter.NumberStringConverter;
import model.Category;
import model.Product;

public class EditProductController implements Initializable {
    @FXML
    public ComboBox<Category> category;
    @FXML
    public Hyperlink categLink;
    @FXML
    public TextField name;
    @FXML
    public TextArea description;
    @FXML
    public TextField cost;
    @FXML
    public TextField image;
    @FXML
    public Tab tab;
    public Product product;
    public Category initCateg;
    public ProductDetailController detailController;
    public boolean reopenDetails;
    public boolean saved;
    public ObservableList<Category> categories;

    public static EditProductController createController(Product prod, ProductDetailController detail) {
        try {
            URL fxmlURL = EditProductController.class.getResource("../view/EditProduct.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            fxmlLoader.<TabPane>load();
            var controller = fxmlLoader.<EditProductController>getController();
            controller.setupFields(prod);
            controller.detailController = detail;
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    public void setupFields(Product prod) {
        product = prod;
        initCateg = MainWindowController.factory.getCategoryDAO().getById(product.getCategory());
        cost.setText(Double.toString(product.getCost()));
        category.getSelectionModel().select(initCateg);
        categLink.setDisable(false);
        name.setText(product.getName());
        description.setText(product.getDescription());
        image.setText(product.getImagePath());
        tab.setText("Editer:" + product.getName());
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        reopenDetails = false;
        categories = FXCollections.observableList(new ArrayList<Category>());
        category.setItems(categories);
        categLink.setOnAction((ActionEvent) -> {
            // TODO open category detail tab
        });
        cost.setTextFormatter(new TextFormatter<>(new NumberStringConverter(Locale.ENGLISH)));
        refreshCateg();
    }

    public void refreshCateg() {
        var c = category.getSelectionModel().getSelectedItem();
        MainWindowController.runAsynchronously(new Runnable() {
            @Override
            public void run() {
                var categList = MainWindowController.factory.getCategoryDAO().getAll();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        categories.clear();
                        categories.addAll(categList);
                        int index = categories.indexOf(c);
                        if (index != -1)
                            category.getSelectionModel().select(index);
                    }
                });
            }
        });
    }

    public void reset() {
        cost.setText(Double.toString(product.getCost()));
        if (category.getItems().contains(initCateg))
            category.getSelectionModel().select(initCateg);
        else
            category.getSelectionModel().select(null);
        categLink.setDisable(false);
        name.setText(product.getName());
        description.setText(product.getDescription());
        image.setText(product.getImagePath());
        tab.setText("Editer:" + product.getName());
    }

    public void cancel() {
        saved = false;
        reopenDetails = true;
        tab.getOnClosed().handle(null);
    }

    public void save() {
        if (category.getSelectionModel().getSelectedItem() == null) {
            new Alert(AlertType.WARNING, "Selectionnez une catégorie");
            return;
        }
        if (Double.parseDouble(cost.getText()) <= 0) {
            new Alert(AlertType.WARNING, "Selectionnez un prix supérieur à zéro");
            return;
        }
        if (name.getText().length() == 0) {
            new Alert(AlertType.WARNING, "Entrez un nom");
            return;
        }
        product.setCategory(category.getSelectionModel().getSelectedItem().getId());
        product.setCost(Double.parseDouble(cost.getText()));
        product.setDescription(description.getText());
        product.setImagePath(image.getText());
        product.setName(name.getText());
        if (MainWindowController.factory.getProductDAO().update(product)) {
            saved = true;
            reopenDetails = true;
            tab.getOnClosed().handle(null);
        } else
            new Alert(AlertType.ERROR, "Impossible de sauvegarder les changement").showAndWait();
    }

}