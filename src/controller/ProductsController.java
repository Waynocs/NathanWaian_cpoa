package controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import model.Product;

public class ProductsController implements Initializable {
    @FXML
    public TableView<Product> table;
    @FXML
    public TableColumn<Product, String> category;
    @FXML
    public TableColumn<Product, String> id;
    @FXML
    public TableColumn<Product, String> name;
    @FXML
    public TableColumn<Product, String> description;
    @FXML
    public TableColumn<Product, String> price;
    @FXML
    public TableColumn<Product, String> image;
    @FXML
    public TableColumn<Product, Void> detail;
    @FXML
    public TableColumn<Product, Void> remove;

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

        category.setCellFactory(new Callback<TableColumn<Product, String>, TableCell<Product, String>>() {
            @Override
            public TableCell<Product, String> call(TableColumn<Product, String> arg0) {
                return new TableCell<Product, String>() {

                    private Hyperlink link = new Hyperlink();

                    {

                        {
                            link.setOnAction((ActionEvent event) -> {
                                // TODO open a category detail tab
                            });
                        }
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        link.setText(item);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(link);
                        }
                    }
                };
            };
        });
        category.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Product, String> arg0) {
                        return new ReadOnlyStringWrapper(MainWindowController.factory.getCategoryDAO()
                                .getById(arg0.getValue().getId()).getName());
                    }
                });
        price.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Product, String> arg0) {
                        return new ReadOnlyStringWrapper(arg0.getValue().getCost() + "€");
                    }
                });
        id.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Product, String> arg0) {
                return new ReadOnlyStringWrapper(Integer.toString(arg0.getValue().getId()));
            }
        });
        name.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Product, String> arg0) {
                        return new ReadOnlyStringWrapper(arg0.getValue().getName());
                    }
                });
        description.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Product, String> arg0) {
                        return new ReadOnlyStringWrapper(arg0.getValue().getDescription());
                    }
                });
        image.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Product, String> arg0) {
                        return new ReadOnlyStringWrapper(arg0.getValue().getImagePath());
                    }
                });
        detail.setCellFactory(new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> arg0) {
                return new TableCell<Product, Void>() {

                    private Button button = new Button();
                    {
                        var iv = new ImageView();
                        iv.setSmooth(false);
                        iv.setPreserveRatio(false);
                        iv.setImage(MainWindowController.detailImage);
                        button.setGraphic(iv);
                        alignmentProperty().set(Pos.BASELINE_CENTER);
                        button.setOnAction((ActionEvent event) -> {
                            // TODO open a product detail tab
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            };
        });
        remove.setCellFactory(new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> arg0) {
                return new TableCell<Product, Void>() {

                    private Button button = new Button();
                    {
                        var iv = new ImageView();
                        iv.setSmooth(false);
                        iv.setPreserveRatio(false);
                        iv.setImage(MainWindowController.removeImage);
                        button.setGraphic(iv);
                        alignmentProperty().set(Pos.BASELINE_CENTER);
                        button.setOnAction((ActionEvent event) -> {
                            if (!MainWindowController.factory.getProductDAO()
                                    .delete(table.getItems().get(getIndex()))) {
                                var alert = new Alert(AlertType.ERROR, "Un erreur est survenue");
                                alert.setTitle("Erreur suppression");
                                alert.showAndWait();
                            } else
                                refresh();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            };
        });
        items = FXCollections.observableList(new ArrayList<Product>());
        table.setItems(items);
        items.addAll(MainWindowController.factory.getProductDAO().getAll());
    }

    public void refresh() {
        items.clear();
        items.addAll(MainWindowController.factory.getProductDAO().getAll());
    }

    private ObservableList<Product> items;
}