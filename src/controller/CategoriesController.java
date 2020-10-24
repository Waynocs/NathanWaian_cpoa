package controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import model.Category;

public class CategoriesController implements Initializable {

    @FXML
    public TableView<Category> table;
    @FXML
    public TableColumn<Category, String> title;
    @FXML
    public TableColumn<Category, String> id;
    @FXML
    public TableColumn<Category, String> image;
    @FXML
    public TableColumn<Category, String> description;
    @FXML
    public TableColumn<Category, Void> detail;
    @FXML
    public TableColumn<Category, Void> remove;
    @FXML
    public TextField searchbar;

    public static Tab createControl() {
        try {
            URL fxmlURL = CategoriesController.class.getResource("../view/Categories.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            return fxmlLoader.<TabPane>load().getTabs().get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        title.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Category, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Category, String> arg0) {
                        return new ReadOnlyStringWrapper(arg0.getValue().getName());
                    }
                });

        id.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Category, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Category, String> arg0) {
                return new ReadOnlyStringWrapper(Integer.toString(arg0.getValue().getId()));
            }
        });

        image.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Category, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Category, String> arg0) {
                        return new ReadOnlyStringWrapper(arg0.getValue().getImagePath());
                    }
                });
        detail.setCellFactory(new Callback<TableColumn<Category, Void>, TableCell<Category, Void>>() {
            @Override
            public TableCell<Category, Void> call(TableColumn<Category, Void> arg0) {
                return new TableCell<Category, Void>() {

                    private Button button = new Button();
                    {
                        var iv = new ImageView();
                        iv.setSmooth(false);
                        iv.setPreserveRatio(false);
                        iv.setImage(MainWindowController.detailImage);
                        button.setGraphic(iv);
                        alignmentProperty().set(Pos.BASELINE_CENTER);
                        button.setOnAction((ActionEvent event) -> {
                            MainWindowController.detailCategory(table.getItems().get(getIndex()));
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
        remove.setCellFactory(new Callback<TableColumn<Category, Void>, TableCell<Category, Void>>() {
            @Override
            public TableCell<Category, Void> call(TableColumn<Category, Void> arg0) {
                return new TableCell<Category, Void>() {

                    private Button button = new Button();
                    {
                        var iv = new ImageView();
                        iv.setSmooth(false);
                        iv.setPreserveRatio(false);
                        iv.setImage(MainWindowController.removeImage);
                        button.setGraphic(iv);
                        alignmentProperty().set(Pos.BASELINE_CENTER);
                        button.setOnAction((ActionEvent event) -> {
                            var categ = table.getItems().get(getIndex());
                            MainWindowController.removeCategory(categ, new Runnable() {

                                @Override
                                public void run() {
                                    allItems.remove(categ);
                                    applySearchKey();
                                }

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
        displayedItems = FXCollections.observableList(new ArrayList<Category>());
        allItems = new LinkedList<Category>();
        table.setItems(displayedItems);
        searchKey = "";
        table.setOnKeyPressed((KeyEvent ev) -> {
            if (ev.getCode() == KeyCode.DELETE) {
                var categ = table.getSelectionModel().getSelectedItem();
                if (categ != null) {
                    MainWindowController.removeCategory(categ, new Runnable() {

                        @Override
                        public void run() {
                            allItems.remove(categ);
                            applySearchKey();
                        }

                    }, null);
                }
            }
        });
        refresh();
    }

    public void refresh() {
        MainWindowController.runAsynchronously(new Runnable() {

            @Override
            public void run() {
                allItems.clear();
                for (Category categ : MainWindowController.factory.getCategoryDAO().getAll())
                    allItems.add(categ);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        applySearchKey();
                    }
                });
            }
        });
    }

    public void search() {
        searchKey = searchbar.getText();
        applySearchKey();
    }

    public void applySearchKey() {
        displayedItems.clear();
        for (Category categ : allItems) {
            boolean toAdd = false;
            if (Utilities.compareStrings(searchKey, categ.getName()))
                toAdd = true;
            else if (Utilities.compareStrings(searchKey, categ.getImagePath()))
                toAdd = true;
            if (toAdd)
                displayedItems.add(categ);
        }
    }

    private String searchKey;
    private List<Category> allItems;
    private ObservableList<Category> displayedItems;
}
