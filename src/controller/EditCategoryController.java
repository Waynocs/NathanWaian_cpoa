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

public class EditCategoryController implements Initializable {

    @FXML
    public TextField title;

    @FXML
    public TextField image;
    public Tab tab;
    public Category category;
    public CategoryDetailController detailController;
    public boolean reopenDetails;
    public boolean saved;

    public static EditCategoryController createController(Category categ, CategoryDetailController detail) {
        try {
            URL fxmlURL = EditCategoryController.class.getResource("../view/EditCategory.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            fxmlLoader.<TabPane>load();
            var controller = fxmlLoader.<EditCategoryController>getController();
            controller.setupFields(categ);
            controller.detailController = detail;
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    public void setupFields(Category categ) {
        category = categ;
        title.setText(category.getName());

        image.setText(category.getImagePath());
        tab.setText("Editer:" + category.getName());

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        reopenDetails = false;

    }

    public void reset() {

        title.setText(category.getName());
        image.setText(category.getImagePath());
        tab.setText("Editer:" + category.getName());
    }

    public void cancel() {
        saved = false;
        reopenDetails = true;
        tab.getOnClosed().handle(null);
    }

    public void save() {
        MainWindowController.runAsynchronously(() -> {
            if (title.getText().length() == 0)
                return new AlertPair("Entrez un titre", AlertType.WARNING);
            category.setImagePath(image.getText());
            category.setName(title.getText());
            try {
                if (MainWindowController.factory.getCategoryDAO().update(category)) {
                    saved = true;
                    reopenDetails = true;
                    tab.getOnClosed().handle(null);
                    return new AlertPair(null, null);
                } else
                    return new AlertPair("Impossible de modifier cette catégorie", AlertType.ERROR);

            } catch (DAOException e) {
                return new AlertPair(e.getMessage(), AlertType.ERROR);
            }
        }, (pair) -> {
            if (pair.getKey() != null)
                new Alert(pair.getValue(), pair.getKey()).showAndWait();
        });
    }
}
