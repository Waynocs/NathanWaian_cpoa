package controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.util.Callback;
import model.Customer;

public class CustomersController implements Initializable {

    @FXML
    public TableView<Customer> table;
    @FXML
    public TableColumn<Customer, String> id;
    @FXML
    public TableColumn<Customer, String> surname;
    @FXML
    public TableColumn<Customer, String> name;
    @FXML
    public TableColumn<Customer, String> identity;
    @FXML
    public TableColumn<Customer, String> password;
    @FXML
    public TableColumn<Customer, String> number;
    @FXML
    public TableColumn<Customer, String> street;
    @FXML
    public TableColumn<Customer, String> postalcode;
    @FXML
    public TableColumn<Customer, String> city;
    @FXML
    public TableColumn<Customer, String> country;
    @FXML
    public TableColumn<Customer, Void> detail;
    @FXML
    public TableColumn<Customer, Void> remove;
    @FXML
    public Label displayCount;
    @FXML
    public ToggleButton filtersButton;
    @FXML
    public BorderPane mainPane;
    @FXML
    public VBox filterPanel;
    @FXML
    public VBox surnameVBox;
    @FXML
    public VBox nameVBox;
    @FXML
    public VBox identityVBox;
    @FXML
    public VBox postalcodeVBox;
    @FXML
    public VBox cityVBox;
    @FXML
    public VBox countryVBox;
    @FXML
    public CheckBox seePass;

    private List<Predicate<Customer>> surnameFilters;
    private List<Predicate<Customer>> nameFilters;
    private List<Predicate<Customer>> identityFilters;
    private List<Predicate<Customer>> postalcodeFilters;
    private List<Predicate<Customer>> cityFilters;
    private List<Predicate<Customer>> countryFilters;
    private List<Customer> allItems;
    private ObservableList<Customer> displayedItems;

    public static Tab createControl() {
        try {
            URL fxmlURL = CustomersController.class.getResource("../view/Customers.fxml");
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
        surnameFilters = new LinkedList<Predicate<Customer>>();
        nameFilters = new LinkedList<Predicate<Customer>>();
        identityFilters = new LinkedList<Predicate<Customer>>();
        postalcodeFilters = new LinkedList<Predicate<Customer>>();
        cityFilters = new LinkedList<Predicate<Customer>>();
        countryFilters = new LinkedList<Predicate<Customer>>();
        seePass.setOnAction((e) -> table.refresh());
        id.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Customer, String> arg0) {
                return new ReadOnlyStringWrapper(Integer.toString(arg0.getValue().getId()));
            }
        });

        surname.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Customer, String> arg0) {
                        return new ReadOnlyStringWrapper(arg0.getValue().getSurname());
                    }
                });

        name.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Customer, String> arg0) {
                        return new ReadOnlyStringWrapper(arg0.getValue().getName());
                    }
                });

        identity.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Customer, String> arg0) {
                        return new ReadOnlyStringWrapper(arg0.getValue().getIdentifier());
                    }
                });

        password.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Customer, String> arg0) {
                        return new ReadOnlyStringWrapper(seePass.isSelected() ? arg0.getValue().getPwd()
                                : Utilities.getHiddenString(arg0.getValue().getPwd()));
                    }
                });

        number.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Customer, String> arg0) {
                        return new ReadOnlyStringWrapper(arg0.getValue().getAddressNumber());
                    }
                });

        street.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Customer, String> arg0) {
                        return new ReadOnlyStringWrapper(arg0.getValue().getAddressStreet());
                    }
                });

        postalcode.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Customer, String> arg0) {
                        return new ReadOnlyStringWrapper(arg0.getValue().getAddressPostalCode());
                    }
                });

        city.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Customer, String> arg0) {
                        return new ReadOnlyStringWrapper(arg0.getValue().getAddressCity());
                    }
                });

        country.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Customer, String> arg0) {
                        return new ReadOnlyStringWrapper(arg0.getValue().getAddressCountry());
                    }
                });

        detail.setCellFactory(new Callback<TableColumn<Customer, Void>, TableCell<Customer, Void>>() {
            @Override
            public TableCell<Customer, Void> call(TableColumn<Customer, Void> arg0) {
                return new TableCell<Customer, Void>() {

                    private Button button = new Button();
                    {
                        var iv = new ImageView();
                        iv.setSmooth(false);
                        iv.setPreserveRatio(false);
                        iv.setImage(MainWindowController.detailImage);
                        button.setGraphic(iv);
                        alignmentProperty().set(Pos.BASELINE_CENTER);
                        button.setOnAction((ActionEvent event) -> {
                            MainWindowController.detailCustomer(table.getItems().get(getIndex()));
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

        remove.setCellFactory(new Callback<TableColumn<Customer, Void>, TableCell<Customer, Void>>() {
            @Override
            public TableCell<Customer, Void> call(TableColumn<Customer, Void> arg0) {
                return new TableCell<Customer, Void>() {

                    private Button button = new Button();
                    {
                        var iv = new ImageView();
                        iv.setSmooth(false);
                        iv.setPreserveRatio(false);
                        iv.setImage(MainWindowController.removeImage);
                        button.setGraphic(iv);
                        alignmentProperty().set(Pos.BASELINE_CENTER);
                        button.setOnAction((ActionEvent event) -> {
                            var custo = table.getItems().get(getIndex());
                            MainWindowController.removeCustomer(custo, () -> {
                                allItems.remove(custo);
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
        displayedItems = FXCollections.observableList(new ArrayList<Customer>());
        allItems = new LinkedList<Customer>();
        table.setItems(displayedItems);
        table.setOnKeyPressed((KeyEvent ev) -> {
            if (ev.getCode() == KeyCode.DELETE) {
                var custo = table.getSelectionModel().getSelectedItem();
                if (custo != null) {
                    MainWindowController.removeCustomer(custo, () -> {
                        allItems.remove(custo);
                        applyFilters();
                    }, null);
                }
            }
        });
        addSurnameFilter();
        addNameFilter();
        addIdentityFilter();
        addPostalcodeFilter();
        addCityFilter();
        addCountryFilter();
        refresh();
    }

    public void refresh() {
        MainWindowController.runAsynchronously(() -> {
            allItems.clear();
            for (Customer custo : MainWindowController.factory.getCustomerDAO().getAll())
                allItems.add(custo);
        }, () -> applyFilters());
    }

    public void filter() {
        surnameFilters.clear();
        nameFilters.clear();
        identityFilters.clear();
        postalcodeFilters.clear();
        cityFilters.clear();
        countryFilters.clear();

        for (Node filter : surnameVBox.getChildren()) {
            var field = (TextField) ((HBox) filter).getChildren().get(0);
            var key = field.getText();
            surnameFilters.add((custo) -> Utilities.compareStrings(key, custo.getSurname()));
        }
        for (Node filter : nameVBox.getChildren()) {
            var field = (TextField) ((HBox) filter).getChildren().get(0);
            var key = field.getText();
            nameFilters.add((custo) -> Utilities.compareStrings(key, custo.getName()));
        }
        for (Node filter : identityVBox.getChildren()) {
            var field = (TextField) ((HBox) filter).getChildren().get(0);
            var key = field.getText();
            identityFilters.add((custo) -> Utilities.compareStrings(key, custo.getIdentifier()));
        }
        for (Node filter : postalcodeVBox.getChildren()) {
            var field = (TextField) ((HBox) filter).getChildren().get(0);
            var key = field.getText();
            postalcodeFilters.add((custo) -> Utilities.compareStrings(key, custo.getAddressPostalCode()));
        }
        for (Node filter : cityVBox.getChildren()) {
            var field = (TextField) ((HBox) filter).getChildren().get(0);
            var key = field.getText();
            cityFilters.add((custo) -> Utilities.compareStrings(key, custo.getAddressCity()));
        }
        for (Node filter : countryVBox.getChildren()) {
            var field = (TextField) ((HBox) filter).getChildren().get(0);
            var key = field.getText();
            countryFilters.add((custo) -> Utilities.compareStrings(key, custo.getAddressCountry()));
        }
        applyFilters();
    }

    public void applyFilters() {
        displayedItems.clear();
        for (Customer custo : allItems) {
            if (Utilities.testAny(surnameFilters, (c) -> c.test(custo), true)
                    && Utilities.testAny(nameFilters, (c) -> c.test(custo), true)
                    && Utilities.testAny(identityFilters, (c) -> c.test(custo), true)
                    && Utilities.testAny(postalcodeFilters, (c) -> c.test(custo), true)
                    && Utilities.testAny(cityFilters, (c) -> c.test(custo), true)
                    && Utilities.testAny(countryFilters, (c) -> c.test(custo), true))
                displayedItems.add(custo);
        }
        displayCount.setText(displayedItems.size() + " / " + allItems.size() + " affiché"
                + (displayedItems.size() != 1 ? "s " : " "));
    }

    public void addSurnameFilter() {
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
        deleteButton.setOnAction((e) -> surnameVBox.getChildren().remove(box));
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(key, deleteButton);
        HBox.setMargin(key, new Insets(5));
        HBox.setMargin(deleteButton, new Insets(5));
        surnameVBox.getChildren().add(box);
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

    public void addIdentityFilter() {
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
        deleteButton.setOnAction((e) -> identityVBox.getChildren().remove(box));
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(key, deleteButton);
        HBox.setMargin(key, new Insets(5));
        HBox.setMargin(deleteButton, new Insets(5));
        identityVBox.getChildren().add(box);

    }

    public void addPostalcodeFilter() {
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
        deleteButton.setOnAction((e) -> postalcodeVBox.getChildren().remove(box));
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(key, deleteButton);
        HBox.setMargin(key, new Insets(5));
        HBox.setMargin(deleteButton, new Insets(5));
        postalcodeVBox.getChildren().add(box);
    }

    public void addCityFilter() {
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
        deleteButton.setOnAction((e) -> cityVBox.getChildren().remove(box));
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(key, deleteButton);
        HBox.setMargin(key, new Insets(5));
        HBox.setMargin(deleteButton, new Insets(5));
        cityVBox.getChildren().add(box);
    }

    public void addCountryFilter() {
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
        deleteButton.setOnAction((e) -> countryVBox.getChildren().remove(box));
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(key, deleteButton);
        HBox.setMargin(key, new Insets(5));
        HBox.setMargin(deleteButton, new Insets(5));
        countryVBox.getChildren().add(box);
    }
}