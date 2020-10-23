package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.DAOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import model.Category;

public class EditCategoryController {
    /*
     * @FXML public TextField title;
     * 
     * @FXML public TextField image; public Tab tab; public Category category;
     * public ProductDetailController detailController; public boolean
     * reopenDetails; public boolean saved;
     * 
     * public static EditCategoryController createController(Category categ,
     * CategoryDetailController detail) { try { URL fxmlURL =
     * EditCategoryController.class.getResource("../view/EditCategory.fxml");
     * FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL); fxmlLoader.<TabPane>load();
     * var controller = fxmlLoader.<EditCategoryController>getController();
     * controller.setupFields(categ); controller.detailController = detail; return
     * controller; } catch (IOException e) { e.printStackTrace(); System.exit(0);
     * return null; } }
     * 
     * public void setupFields(Category categ) { category = categ;
     * title.setText(category.getName());
     * 
     * image.setText(category.getImagePath()); tab.setText("Editer:" +
     * category.getName()); MainWindowController.runAsynchronously(new Runnable() {
     * 
     * @Override public void run() {
     * 
     * Platform.runLater(new Runnable() {
     * 
     * @Override public void run() {
     * 
     * category.getSelectionModel().select(initCateg); refreshCateg(); } }); } }); }
     * 
     * @Override public void initialize(URL arg0, ResourceBundle arg1) {
     * reopenDetails = false;
     * 
     * }
     * 
     * public void reset() {
     * 
     * title.setText(category.getName()); image.setText(category.getImagePath());
     * tab.setText("Editer:" + category.getName()); }
     * 
     * public void cancel() { saved = false; reopenDetails = true;
     * tab.getOnClosed().handle(null); }
     * 
     * public void save() {
     * 
     * if (title.getText().length() == 0) { new Alert(AlertType.WARNING,
     * "Entrez un titre").showAndWait(); return; }
     * category.setImagePath(image.getText()); category.setName(title.getText());
     * try { if (MainWindowController.factory.getCategoryDAO().update(category)) {
     * saved = true; reopenDetails = true; tab.getOnClosed().handle(null); } else
     * new Alert(AlertType.ERROR,
     * "Impossible de modifier ce produit").showAndWait();
     * 
     * } catch (DAOException e) { new Alert(AlertType.ERROR,
     * e.getMessage()).showAndWait(); } }
     */
}
