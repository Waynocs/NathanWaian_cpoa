package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.DAOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import model.Category;

public class NewCategoryController implements Initializable {

    @FXML
    public TextField title;
    @FXML
    public TextField image;
    @FXML
    public Tab tab;

    public static Tab createControl() {
        try {
            URL fxmlURL = CategoriesController.class.getResource("../view/NewCategory.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            return fxmlLoader.<TabPane>load().getTabs().get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    public void cancel() {
        MainWindowController.getMainTabPane().getTabs().remove(tab);
    }

    public void save() {
        MainWindowController.runAsynchronously(() -> {
            if (title.getText().length() == 0)
                return new AlertPair("Entrez un titre", AlertType.WARNING);
            var category = new Category(title.getText(), image.getText(), 0);

            try {
                if (MainWindowController.factory.getCategoryDAO().create(category) != null)
                    return new AlertPair(null, null);
                else
                    return new AlertPair("Impossible de créer cette catégorie", AlertType.ERROR);
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
