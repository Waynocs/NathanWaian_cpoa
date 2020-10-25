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

public class NewProductController implements Initializable {
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
    public ObservableList<Category> categories;

    public static Tab createControl() {
        try {
            URL fxmlURL = CategoriesController.class.getResource("../view/NewProduct.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            return fxmlLoader.<TabPane>load().getTabs().get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        categories = FXCollections.observableList(new ArrayList<Category>());
        category.setItems(categories);
        categLink.setOnAction(
                (e) -> MainWindowController.detailCategory(category.getSelectionModel().getSelectedItem()));
        cost.setTextFormatter(new TextFormatter<>(new NumberStringConverter(Locale.ENGLISH)));
        refreshCateg();
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

    public void cancel() {
        MainWindowController.getMainTabPane().getTabs().remove(tab);
    }

    public void categoryChanged() {
        if (category.getSelectionModel().getSelectedIndex() == -1)
            categLink.setDisable(true);
        else
            categLink.setDisable(false);
    }

    public void save() {
        MainWindowController.runAsynchronously(() -> {
            if (category.getSelectionModel().getSelectedItem() == null)
                return new AlertPair("Selectionnez une catégorie", AlertType.WARNING);
            if (cost.getText().length() == 0 || Double.parseDouble(cost.getText()) <= 0) {
                return new AlertPair("Selectionnez un prix supérieur à zéro", AlertType.WARNING);
            }
            if (name.getText().length() == 0) {
                return new AlertPair("Entrez un nom", AlertType.WARNING);
            }
            var product = new Product(0, name.getText(), Double.parseDouble(cost.getText()), description.getText(),
                    category.getSelectionModel().getSelectedItem().getId(), image.getText());
            try {
                if (MainWindowController.factory.getProductDAO().create(product) != null)
                    return new AlertPair();
                else
                    return new AlertPair("Impossible de créer ce produit", AlertType.ERROR);
            } catch (DAOException e) {
                return new AlertPair(e.getMessage(), AlertType.ERROR);
            }
        }, (pair) -> {
            if (pair.getKey() != null)
                new Alert(pair.getValue(), pair.getKey()).showAndWait();
            else
                MainWindowController.getMainTabPane().getTabs().remove(tab);
        });
    }

}