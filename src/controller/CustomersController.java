package controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    public TextField searchbar;

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
                        return new ReadOnlyStringWrapper(arg0.getValue().getPwd());
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
        displayedItems = FXCollections.observableList(new ArrayList<Customer>());
        allItems = new LinkedList<Customer>();
        table.setItems(displayedItems);
        searchKey = "";
        table.setOnKeyPressed((KeyEvent ev) -> {
            if (ev.getCode() == KeyCode.DELETE) {
                var custo = table.getSelectionModel().getSelectedItem();
                if (custo != null) {
                    MainWindowController.removeCustomer(custo, () -> {
                        allItems.remove(custo);
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
            for (Customer custo : MainWindowController.factory.getCustomerDAO().getAll())
                allItems.add(custo);
        }, () -> applySearchKey());
    }

    public void search() {
        searchKey = searchbar.getText();
        applySearchKey();
    }

    public void applySearchKey() {
        displayedItems.clear();
        for (Customer custo : allItems) {
            boolean toAdd = false;
            if (Utilities.compareStrings(searchKey, custo.getSurname()))
                toAdd = true;
            else if (Utilities.compareStrings(searchKey, custo.getName()))
                toAdd = true;
            else if (Utilities.compareStrings(searchKey, custo.getIdentifier()))
                toAdd = true;
            else if (Utilities.compareStrings(searchKey, custo.getPwd()))
                toAdd = true;
            else if (Utilities.compareStrings(searchKey, custo.getAddressNumber()))
                toAdd = true;
            else if (Utilities.compareStrings(searchKey, custo.getAddressStreet()))
                toAdd = true;
            else if (Utilities.compareStrings(searchKey, custo.getAddressPostalCode()))
                toAdd = true;
            else if (Utilities.compareStrings(searchKey, custo.getAddressCity()))
                toAdd = true;
            else if (Utilities.compareStrings(searchKey, custo.getAddressCountry()))
                toAdd = true;
            if (toAdd)
                displayedItems.add(custo);
        }
    }

    private String searchKey;
    private List<Customer> allItems;
    private ObservableList<Customer> displayedItems;
}