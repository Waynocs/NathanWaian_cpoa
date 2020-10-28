package controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    public Label displayCount;
    @FXML
    public ToggleButton filtersButton;
    @FXML
    public BorderPane mainPane;
    @FXML
    public VBox filterPanel;
    @FXML
    public VBox imageVBox;
    @FXML
    public VBox titleVBox;

    private List<Predicate<Category>> imageFilters;
    private List<Predicate<Category>> titleFilters;
    private List<Category> allItems;
    private ObservableList<Category> displayedItems;

    public static Tab createControl() {
        try {
            URL fxmlURL = CategoriesController.class.getResource("../view/Categories.fxml");
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

        filtersButton.setOnAction((e) -> mainPane.setRight(filtersButton.isSelected() ? filterPanel : null));
        mainPane.setRight(null);
        titleFilters = new LinkedList<Predicate<Category>>();
        imageFilters = new LinkedList<Predicate<Category>>();

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
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        button.setOnAction((ActionEvent event) -> {
                            MainWindowController.detailCategory(table.getItems().get(getIndex()));
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
                            MainWindowController.removeCategory(categ, () -> {
                                allItems.remove(categ);
                                applyFilters();
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
        table.setOnKeyPressed((KeyEvent ev) -> {
            if (ev.getCode() == KeyCode.DELETE) {
                var categ = table.getSelectionModel().getSelectedItem();
                if (categ != null) {
                    MainWindowController.removeCategory(categ, () -> {
                        allItems.remove(categ);
                        applyFilters();
                    }, null);
                }
            }
        });
        addTitleFilter();
        addImageFilter();
        refresh();
    }

    public void refresh() {
        MainWindowController.runAsynchronously(() -> {
            allItems.clear();
            for (Category categ : MainWindowController.factory.getCategoryDAO().getAll())
                allItems.add(categ);
        }, () -> applyFilters());
    }

    public void filter() {
        titleFilters.clear();
        imageFilters.clear();

        for (Node filter : titleVBox.getChildren()) {
            var field = (TextField) ((HBox) filter).getChildren().get(0);
            var key = field.getText();
            titleFilters.add((categ) -> Utilities.compareStrings(key, categ.getName()));
        }
        for (Node filter : imageVBox.getChildren()) {
            var field = (TextField) ((HBox) filter).getChildren().get(0);
            var key = field.getText();
            imageFilters.add((categ) -> Utilities.compareStrings(key, categ.getImagePath()));
        }
        applyFilters();
    }

    public void applyFilters() {
        displayedItems.clear();
        for (Category categ : allItems) {
            if (Utilities.testAny(titleFilters, (c) -> c.test(categ), true)
                    && Utilities.testAny(imageFilters, (c) -> c.test(categ), true))
                displayedItems.add(categ);
        }
        displayCount.setText(displayedItems.size() + " / " + allItems.size() + " affiché"
                + (displayedItems.size() != 1 ? "s " : " "));
    }

    public void addTitleFilter() {
        var key = new TextField();
        var deleteButton = new Button();
        var img = new ImageView(MainWindowController.removeImage);
        img.setPreserveRatio(false);
        img.setSmooth(false);
        img.setFitHeight(24);
        img.setFitWidth(24);
        deleteButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        deleteButton.setGraphic(img);
        var box = new HBox();
        deleteButton.setOnAction((e) -> titleVBox.getChildren().remove(box));
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(key, deleteButton);
        HBox.setMargin(key, new Insets(5));
        HBox.setMargin(deleteButton, new Insets(5));
        titleVBox.getChildren().add(box);
    }

    public void addImageFilter() {
        var key = new TextField();
        var deleteButton = new Button();
        var img = new ImageView(MainWindowController.removeImage);
        img.setPreserveRatio(false);
        img.setSmooth(false);
        img.setFitHeight(24);
        img.setFitWidth(24);
        deleteButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        deleteButton.setGraphic(img);
        var box = new HBox();
        deleteButton.setOnAction((e) -> imageVBox.getChildren().remove(box));
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(key, deleteButton);
        HBox.setMargin(key, new Insets(5));
        HBox.setMargin(deleteButton, new Insets(5));
        imageVBox.getChildren().add(box);
    }

}
