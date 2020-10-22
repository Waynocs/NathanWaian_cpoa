package controller;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import model.Customer;
import model.Order;

public class OrdersController implements Initializable {
    @FXML
    public TableView<Order> table;
    @FXML
    public TableColumn<Order, String> customer;
    @FXML
    public TableColumn<Order, String> id;
    @FXML
    public TableColumn<Order, String> date;
    @FXML
    public TableColumn<Order, Void> detail;
    @FXML
    public TableColumn<Order, Void> remove;
    @FXML
    public TextField searchbar;
    private String searchKey;
    private List<Order> allItems;
    private Map<Integer, Customer> allCusts;
    private ObservableList<Order> displayedItems;

    public static Tab createControl() {
        try {
            URL fxmlURL = ProductsController.class.getResource("../view/Orders.fxml");
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

        customer.setCellFactory(new Callback<TableColumn<Order, String>, TableCell<Order, String>>() {
            @Override
            public TableCell<Order, String> call(TableColumn<Order, String> arg0) {
                return new TableCell<Order, String>() {

                    private Hyperlink link = new Hyperlink();

                    {

                        {
                            link.setOnAction((ActionEvent event) -> {
                                // TODO open a customer detail tab
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
        customer.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Order, String> arg0) {
                        var cust = allCusts.get(arg0.getValue().getCustomer());
                        return new ReadOnlyStringWrapper(cust.getName() + " " + cust.getSurname());
                    }
                });
        date.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Order, String> arg0) {
                return new ReadOnlyStringWrapper(
                        arg0.getValue().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm")));
            }
        });
        id.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Order, String> arg0) {
                return new ReadOnlyStringWrapper(Integer.toString(arg0.getValue().getId()));
            }
        });
        detail.setCellFactory(new Callback<TableColumn<Order, Void>, TableCell<Order, Void>>() {
            @Override
            public TableCell<Order, Void> call(TableColumn<Order, Void> arg0) {
                return new TableCell<Order, Void>() {

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
                            MainWindowController.detailOrder(table.getItems().get(getIndex()));
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
        remove.setCellFactory(new Callback<TableColumn<Order, Void>, TableCell<Order, Void>>() {
            @Override
            public TableCell<Order, Void> call(TableColumn<Order, Void> arg0) {
                return new TableCell<Order, Void>() {

                    private Button button = new Button();
                    {
                        var iv = new ImageView();
                        iv.setSmooth(false);
                        iv.setPreserveRatio(false);
                        iv.setImage(MainWindowController.removeImage);
                        button.setGraphic(iv);
                        alignmentProperty().set(Pos.BASELINE_CENTER);
                        button.setOnAction((ActionEvent event) -> {
                            var ord = table.getItems().get(getIndex());
                            MainWindowController.removeOrder(ord, new Runnable() {

                                @Override
                                public void run() {
                                    allItems.remove(ord);
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
        displayedItems = FXCollections.observableList(new ArrayList<Order>());
        allItems = new LinkedList<Order>();
        allCusts = new HashMap<Integer, Customer>();
        table.setItems(displayedItems);
        searchKey = "";
        table.setOnKeyPressed((KeyEvent ev) -> {
            if (ev.getCode() == KeyCode.DELETE) {
                var ord = table.getSelectionModel().getSelectedItem();
                if (ord != null) {
                    MainWindowController.removeOrder(ord, new Runnable() {

                        @Override
                        public void run() {
                            allItems.remove(ord);
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
                allCusts.clear();
                for (Order ord : MainWindowController.factory.getOrderDAO().getAll())
                    allItems.add(ord);
                for (Customer cust : MainWindowController.factory.getCustomerDAO().getAll())
                    allCusts.put(cust.getId(), cust);
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
        for (Order ord : allItems) {
            boolean toAdd = false;
            if (Utilities.compareStrings(searchKey,
                    ord.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm"))))
                toAdd = true;
            else if (Utilities.compareStrings(searchKey, "" + ord.getId()))
                toAdd = true;
            else if (Utilities.compareStrings(searchKey, allCusts.get(ord.getCustomer()).getName()))
                toAdd = true;
            if (toAdd)
                displayedItems.add(ord);
        }
    }

}