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
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import model.Category;
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
    @FXML
    public TextField searchbar;
    private String searchKey;
    private List<Product> allItems;
    private Map<Integer, Category> allCategs;
    private ObservableList<Product> displayedItems;

    public static Tab createControl() {
        try {
            URL fxmlURL = ProductsController.class.getResource("../view/Products.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            return fxmlLoader.<TabPane>load().getTabs().get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
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
                            link.setOnAction((ActionEvent event) -> MainWindowController
                                    .detailCategory(allCategs.get(table.getItems().get(getIndex()).getId())));
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
                        return new ReadOnlyStringWrapper(allCategs.get(arg0.getValue().getCategory()).getName());
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
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        button.setOnAction((ActionEvent event) -> {
                            MainWindowController.detailProduct(table.getItems().get(getIndex()));
                        });
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
                            var prod = table.getItems().get(getIndex());
                            MainWindowController.removeProduct(prod, () -> {
                                allItems.remove(prod);
                                applySearchKey();
                            }, null);
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
        displayedItems = FXCollections.observableList(new ArrayList<Product>());
        allItems = new LinkedList<Product>();
        allCategs = new HashMap<Integer, Category>();
        table.setItems(displayedItems);
        searchKey = "";
        table.setOnKeyPressed((KeyEvent ev) -> {
            if (ev.getCode() == KeyCode.DELETE) {
                var prod = table.getSelectionModel().getSelectedItem();
                if (prod != null) {
                    MainWindowController.removeProduct(prod, () -> {
                        allItems.remove(prod);
                        applySearchKey();
                    }, null);
                }
            }
        });
        refresh();
    }

    public void refresh() {
        MainWindowController.runAsynchronously(() -> {
            allItems.clear();
            allCategs.clear();
            for (Category categ : MainWindowController.factory.getCategoryDAO().getAll())
                allCategs.put(categ.getId(), categ);
            for (Product prod : MainWindowController.factory.getProductDAO().getAll())
                allItems.add(prod);
        }, () -> applySearchKey());
    }

    public void search() {
        searchKey = searchbar.getText();
        applySearchKey();
    }

    public void applySearchKey() {
        displayedItems.clear();
        for (Product prod : allItems) {
            boolean toAdd = false;
            if (Utilities.compareStrings(searchKey, prod.getName()))
                toAdd = true;
            else if (Utilities.compareStrings(searchKey, prod.getImagePath()))
                toAdd = true;
            else if (Utilities.compareStrings(searchKey, prod.getDescription()))
                toAdd = true;
            else if (Utilities.compareStrings(searchKey, allCategs.get(prod.getCategory()).getName()))
                toAdd = true;
            if (toAdd)
                displayedItems.add(prod);
        }
    }

}