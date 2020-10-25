package controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import dao.DAOException;
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
        cost.setText(Double.toString(product.getCost()));
        categLink.setDisable(false);
        name.setText(product.getName());
        description.setText(product.getDescription());
        image.setText(product.getImagePath());
        tab.setText("Editer:" + product.getName());
        MainWindowController.runAsynchronously(
                () -> initCateg = MainWindowController.factory.getCategoryDAO().getById(product.getCategory()), () -> {
                    categories.add(initCateg);
                    category.getSelectionModel().select(initCateg);
                    refreshCateg();
                });
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
    }

    public void refreshCateg() {
        var c = category.getSelectionModel().getSelectedItem();
        MainWindowController.runAsynchronously(() -> MainWindowController.factory.getCategoryDAO().getAll(),
                (categList) -> {
                    categories.clear();
                    categories.addAll(categList);
                    int index = categories.indexOf(c);
                    if (index != -1) {
                        categLink.setDisable(false);
                        category.getSelectionModel().select(index);
                    } else
                        categLink.setDisable(true);
                });
    }

    public void reset() {
        cost.setText(Double.toString(product.getCost()));
        if (category.getItems().contains(initCateg)) {
            category.getSelectionModel().select(initCateg);
            categLink.setDisable(false);
        } else {
            category.getSelectionModel().select(null);
            categLink.setDisable(true);
        }
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

    public void categoryChanged() {
        categLink.setDisable(false);
    }

    public void save() {
        MainWindowController.runAsynchronously(() -> {
            if (category.getSelectionModel().getSelectedItem() == null) {
                return new AlertPair("Selectionnez une catégorie", AlertType.WARNING);
            }
            if (cost.getText().length() == 0 || Double.parseDouble(cost.getText()) <= 0) {
                return new AlertPair("Selectionnez un prix supérieur à zéro", AlertType.WARNING);
            }
            if (name.getText().length() == 0) {
                return new AlertPair("Entrez un nom", AlertType.WARNING);
            }
            product.setCategory(category.getSelectionModel().getSelectedItem().getId());
            product.setCost(Double.parseDouble(cost.getText()));
            product.setDescription(description.getText());
            product.setImagePath(image.getText());
            product.setName(name.getText());
            try {
                if (MainWindowController.factory.getProductDAO().update(product)) {
                    saved = true;
                    reopenDetails = true;
                    tab.getOnClosed().handle(null);
                    return new AlertPair(null, null);
                } else {
                    return new AlertPair("Impossible de modifier ce produit", AlertType.ERROR);
                }

            } catch (DAOException e) {
                return new AlertPair(e.getMessage(), AlertType.ERROR);
            }
        }, (pair) -> {
            if (pair.getKey() != null)
                new Alert(pair.getValue(), pair.getKey()).showAndWait();
        });
    }
}