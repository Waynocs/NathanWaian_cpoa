package controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
    public ToggleButton filtersButton;
    @FXML
    public VBox filterPanel;
    @FXML
    public BorderPane mainPane;
    @FXML
    public VBox categoryVBox;
    @FXML
    public VBox nameVBox;
    @FXML
    public VBox descriptionVBox;
    @FXML
    public VBox priceVBox;
    @FXML
    public VBox imageVBox;

    private List<Product> allItems;
    private Map<Integer, Category> allCategs;
    private ObservableList<Product> displayedItems;
    private ObservableList<Category> categFilterList;
    private List<Predicate<Product>> categoryFilters;
    private List<Predicate<Product>> nameFilters;
    private List<Predicate<Product>> descriptionFilters;
    private List<Predicate<Product>> priceFilters;
    private List<Predicate<Product>> imageFilters;

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
        filtersButton.setOnAction((e) -> mainPane.setRight(filtersButton.isSelected() ? filterPanel : null));
        mainPane.setRight(null);
        categoryFilters = new LinkedList<Predicate<Product>>();
        nameFilters = new LinkedList<Predicate<Product>>();
        descriptionFilters = new LinkedList<Predicate<Product>>();
        priceFilters = new LinkedList<Predicate<Product>>();
        imageFilters = new LinkedList<Predicate<Product>>();
        categFilterList = FXCollections.observableList(new LinkedList<Category>());
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
                                {
                                    double min = allItems.get(0).getCost();
                                    double max = allItems.get(0).getCost();
                                    for (Product product : allItems) {
                                        if (min > product.getCost())
                                            min = product.getCost();
                                        if (max < product.getCost())
                                            max = product.getCost();
                                    }
                                    for (Node node : priceVBox.getChildren()) {
                                        var sliderMin = (Slider) ((VBox) node).getChildren().get(0);
                                        var sliderMax = (Slider) ((VBox) node).getChildren().get(1);
                                        if (sliderMin.getValue() < min)
                                            sliderMin.setValue(min);
                                        if (sliderMin.getValue() > max)
                                            sliderMin.setValue(max);
                                        if (sliderMax.getValue() < min)
                                            sliderMax.setValue(min);
                                        if (sliderMax.getValue() > max)
                                            sliderMax.setValue(max);
                                        sliderMin.setMin(min);
                                        sliderMin.setMax(max);
                                        sliderMax.setMin(min);
                                        sliderMax.setMax(max);
                                    }
                                }
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
        displayedItems = FXCollections.observableList(new ArrayList<Product>());
        allItems = new LinkedList<Product>();
        allCategs = new HashMap<Integer, Category>();
        table.setItems(displayedItems);
        table.setOnKeyPressed((KeyEvent ev) -> {
            if (ev.getCode() == KeyCode.DELETE) {
                var prod = table.getSelectionModel().getSelectedItem();
                if (prod != null) {
                    MainWindowController.removeProduct(prod, () -> {
                        allItems.remove(prod);
                        {
                            double min = allItems.get(0).getCost();
                            double max = allItems.get(0).getCost();
                            for (Product product : allItems) {
                                if (min > product.getCost())
                                    min = product.getCost();
                                if (max < product.getCost())
                                    max = product.getCost();
                            }
                            for (Node node : priceVBox.getChildren()) {
                                var sliderMin = (Slider) ((VBox) node).getChildren().get(0);
                                var sliderMax = (Slider) ((VBox) node).getChildren().get(1);
                                if (sliderMin.getValue() < min)
                                    sliderMin.setValue(min);
                                if (sliderMin.getValue() > max)
                                    sliderMin.setValue(max);
                                if (sliderMax.getValue() < min)
                                    sliderMax.setValue(min);
                                if (sliderMax.getValue() > max)
                                    sliderMax.setValue(max);
                                sliderMin.setMin(min);
                                sliderMin.setMax(max);
                                sliderMax.setMin(min);
                                sliderMax.setMax(max);
                            }
                        }
                        applyFilters();
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
        }, () -> {
            categFilterList.clear();
            categFilterList.addAll(allCategs.values());
            for (Node node : categoryVBox.getChildren()) {
                var box = (ComboBox<Category>) ((HBox) node).getChildren().get(0);
                box.getSelectionModel().select((Category) box.getUserData());
            }
            {
                double min = allItems.get(0).getCost();
                double max = allItems.get(0).getCost();
                for (Product product : allItems) {
                    if (min > product.getCost())
                        min = product.getCost();
                    if (max < product.getCost())
                        max = product.getCost();
                }
                for (Node node : priceVBox.getChildren()) {
                    var sliderMin = (Slider) ((VBox) node).getChildren().get(0);
                    var sliderMax = (Slider) ((VBox) node).getChildren().get(1);
                    if (sliderMin.getValue() < min)
                        sliderMin.setValue(min);
                    if (sliderMin.getValue() > max)
                        sliderMin.setValue(max);
                    if (sliderMax.getValue() < min)
                        sliderMax.setValue(min);
                    if (sliderMax.getValue() > max)
                        sliderMax.setValue(max);
                    sliderMin.setMin(min);
                    sliderMin.setMax(max);
                    sliderMax.setMin(min);
                    sliderMax.setMax(max);
                }
            }
            applyFilters();
        });
    }

    public void filter() {
        categoryFilters.clear();
        nameFilters.clear();
        descriptionFilters.clear();
        priceFilters.clear();
        imageFilters.clear();
        for (Node filter : categoryVBox.getChildren()) {
            var box = (ComboBox<Category>) ((HBox) filter).getChildren().get(0);
            var categ = box.getSelectionModel().getSelectedItem();
            if (categ != null)
                categoryFilters.add((prod) -> prod.getCategory() == categ.getId());
        }
        for (Node filter : nameVBox.getChildren()) {
            var field = (TextField) ((HBox) filter).getChildren().get(0);
            var key = field.getText();
            nameFilters.add((prod) -> Utilities.compareStrings(key, prod.getName()));
        }
        for (Node filter : descriptionVBox.getChildren()) {
            var field = (TextArea) ((HBox) filter).getChildren().get(0);
            var key = field.getText();
            nameFilters.add((prod) -> Utilities.compareStrings(key, prod.getDescription()));
        }
        for (Node node : priceVBox.getChildren()) {
            var sliderMin = (Slider) ((VBox) node).getChildren().get(0);
            var sliderMax = (Slider) ((VBox) node).getChildren().get(1);
            priceFilters
                    .add((prod) -> prod.getCost() >= sliderMin.getValue() && prod.getCost() <= sliderMax.getValue());
        }
        for (Node filter : imageVBox.getChildren()) {
            var field = (TextField) ((HBox) filter).getChildren().get(0);
            var key = field.getText();
            imageFilters.add((prod) -> Utilities.compareStrings(key, prod.getImagePath()));
        }
        applyFilters();
    }

    public void applyFilters() {
        displayedItems.clear();
        for (Product prod : allItems) {
            if (Utilities.testAny(categoryFilters, (p) -> p.test(prod), true)
                    && Utilities.testAny(nameFilters, (p) -> p.test(prod), true)
                    && Utilities.testAny(descriptionFilters, (p) -> p.test(prod), true)
                    && Utilities.testAny(priceFilters, (p) -> p.test(prod), true)
                    && Utilities.testAny(imageFilters, (p) -> p.test(prod), true))
                displayedItems.add(prod);
        }
    }

    public void addCategFilter() {
        var categs = new ComboBox<Category>();
        categs.setItems(categFilterList);
        categs.setOnAction((e) -> {
            if (categs.getSelectionModel().getSelectedIndex() != -1)
                categs.setUserData(categs.getSelectionModel().getSelectedItem());
        });
        var deleteButton = new Button();
        var img = new ImageView(MainWindowController.removeImage);
        img.setPreserveRatio(false);
        img.setSmooth(false);
        img.setFitHeight(24);
        img.setFitWidth(24);
        deleteButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        deleteButton.setGraphic(img);
        var box = new HBox();
        deleteButton.setOnAction((e) -> categoryVBox.getChildren().remove(box));
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(categs, deleteButton);
        HBox.setMargin(categs, new Insets(5));
        HBox.setMargin(deleteButton, new Insets(5));
        categoryVBox.getChildren().add(box);
    }

    public void addNameFilter() {
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
        deleteButton.setOnAction((e) -> nameVBox.getChildren().remove(box));
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(key, deleteButton);
        HBox.setMargin(key, new Insets(5));
        HBox.setMargin(deleteButton, new Insets(5));
        nameVBox.getChildren().add(box);
    }

    public void addDescriptionFilter() {
        var key = new TextArea();
        key.setPrefSize(150, 60);
        var deleteButton = new Button();
        var img = new ImageView(MainWindowController.removeImage);
        img.setPreserveRatio(false);
        img.setSmooth(false);
        img.setFitHeight(24);
        img.setFitWidth(24);
        deleteButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        deleteButton.setGraphic(img);
        var box = new HBox();
        deleteButton.setOnAction((e) -> descriptionVBox.getChildren().remove(box));
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(key, deleteButton);
        HBox.setMargin(key, new Insets(5));
        HBox.setMargin(deleteButton, new Insets(5));
        descriptionVBox.getChildren().add(box);
    }

    public void addPriceFilter() {
        var value = new Label();
        var deleteButton = new Button();
        var img = new ImageView(MainWindowController.removeImage);
        img.setPreserveRatio(false);
        img.setSmooth(false);
        img.setFitHeight(24);
        img.setFitWidth(24);
        deleteButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        deleteButton.setGraphic(img);
        double min = allItems.get(0).getCost();
        double max = allItems.get(0).getCost();
        for (Product product : allItems) {
            if (min > product.getCost())
                min = product.getCost();
            if (max < product.getCost())
                max = product.getCost();
        }
        var box = new VBox();
        var sliderMin = new Slider();
        box.getChildren().add(sliderMin);
        sliderMin.setShowTickLabels(true);
        sliderMin.setShowTickMarks(true);
        sliderMin.setBlockIncrement(.01);
        sliderMin.setMin(min);
        sliderMin.setMax(max);
        sliderMin.setValue(min);
        VBox.setMargin(sliderMin, new Insets(5));
        var sliderMax = new Slider();
        box.getChildren().add(sliderMax);
        sliderMax.setShowTickLabels(true);
        sliderMax.setShowTickMarks(true);
        sliderMax.setBlockIncrement(.01);
        sliderMax.setMin(min);
        sliderMax.setMax(max);
        sliderMax.setValue(max);
        VBox.setMargin(sliderMax, new Insets(5));
        deleteButton.setOnAction((e) -> priceVBox.getChildren().remove(box));
        {
            var detailBox = new HBox();
            box.getChildren().add(detailBox);
            HBox.setMargin(value, new Insets(5));
            HBox.setMargin(deleteButton, new Insets(5));
            detailBox.getChildren().addAll(value, deleteButton);
        }
        box.getChildren().add(new Separator(Orientation.HORIZONTAL));
        Runnable update = () -> {
            value.setText(String.format("%.2f", sliderMin.getValue()) + " € - "
                    + String.format("%.2f", sliderMax.getValue()) + " €");
            if (sliderMin.getValue() > sliderMax.getValue())
                value.setTextFill(Color.RED);
            else
                value.setTextFill(Color.BLACK);
        };
        update.run();
        sliderMin.valueProperty().addListener((arg0, arg1, arg2) -> update.run());
        sliderMax.valueProperty().addListener((arg0, arg1, arg2) -> update.run());
        priceVBox.getChildren().add(box);
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