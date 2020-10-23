package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import model.Category;
/*
 * public class CategoryDetailController implements Initializable {
 * 
 * @FXML public Label title;
 * 
 * @FXML public Label id;
 * 
 * @FXML public Label image; public Tab tab; public Category category;
 * 
 * public static Tab createControl(Category categ) { try { URL fxmlURL =
 * CategoryDetailController.class.getResource("../view/CategoryDetail.fxml");
 * FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL); var control =
 * fxmlLoader.<TabPane>load(); var controller =
 * fxmlLoader.<CategoryDetailController>getController();
 * controller.setupFields(categ); return control.getTabs().get(0); } catch
 * (IOException e) { e.printStackTrace(); System.exit(0); return null; } }
 * 
 * public void setupFields(Category categ) { tab.setText("Détail:" +
 * category.getName());
 * 
 * title.setText("Titre : " + category.getName()); id.setText("ID : " +
 * category.getId()); image.setText("Visuel : " + category.getImagePath()); }
 * 
 * @Override public void initialize(URL arg0, ResourceBundle arg1) { // TODO
 * Auto-generated method stub
 * 
 * }
 * 
 * public void refresh() { MainWindowController.runAsynchronously(new Runnable()
 * {
 * 
 * @Override public void run() { category =
 * MainWindowController.factory.getCategoryDAO().getById(category.getId());
 * Platform.runLater(new Runnable() {
 * 
 * @Override public void run() { tab.setText("Détail:" + category.getName());
 * title.setText("Nom : " + category.getName()); id.setText("ID : " +
 * category.getId()); image.setText("Visuel : " + category.getImagePath()); }
 * }); } }); }
 * 
 * public void edit() { MainWindowController.editCategory(category, this); }
 * 
 * public void remove() { MainWindowController.removeCategory(category, new
 * Runnable() {
 * 
 * @Override public void run() { EventHandler<Event> handler =
 * tab.getOnClosed(); if (null != handler) { handler.handle(null); } else {
 * tab.getTabPane().getTabs().remove(tab); } }
 * 
 * }, null); }
 * 
 * }
 */