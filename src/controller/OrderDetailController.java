package controller;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import javafx.util.Pair;
import model.Order;
import model.OrderLine;
import model.Product;

public class OrderDetailController implements Initializable {
    @FXML
    public Label id;
    @FXML
    public Hyperlink customer;
    @FXML
    public Label date;
    @FXML
    public Label time;
    @FXML
    public TableView<OrderLine> table;
    @FXML
    public TableColumn<OrderLine, String> products;
    @FXML
    public TableColumn<OrderLine, String> quantities;
    @FXML
    public TableColumn<OrderLine, String> prices;
    @FXML
    public TableColumn<OrderLine, String> totals;
    @FXML
    public Label totalCost;
    @FXML
    public Tab tab;
    public Order order;
    public ObservableList<OrderLine> displayedLines;
    public Map<Integer, Product> allProducts;

    public static Tab createControl(Order ord) {
        try {
            URL fxmlURL = ProductDetailController.class.getResource("../view/OrderDetail.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            var control = fxmlLoader.<TabPane>load();
            var controller = fxmlLoader.<OrderDetailController>getController();
            controller.setupFields(ord);
            return control.getTabs().get(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    public void setupFields(Order ord) {
        order = ord;
        refresh();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        displayedLines = FXCollections.observableList(new ArrayList<OrderLine>());
        table.setItems(displayedLines);
        allProducts = new HashMap<Integer, Product>();
        products.setCellFactory(new Callback<TableColumn<OrderLine, String>, TableCell<OrderLine, String>>() {
            @Override
            public TableCell<OrderLine, String> call(TableColumn<OrderLine, String> arg0) {
                return new TableCell<OrderLine, String>() {

                    private Hyperlink link = new Hyperlink();

                    {

                        {
                            link.setOnAction((ActionEvent event) -> {
                                MainWindowController
                                        .detailProduct(allProducts.get(table.getItems().get(getIndex()).getProduct()));
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
        products.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<OrderLine, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<OrderLine, String> arg0) {
                        return new ReadOnlyStringWrapper(allProducts.get(arg0.getValue().getProduct()).getName());
                    }
                });
        quantities.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<OrderLine, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<OrderLine, String> arg0) {
                        return new ReadOnlyStringWrapper(Integer.toString(arg0.getValue().getQuantity()));
                    }
                });
        prices.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<OrderLine, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<OrderLine, String> arg0) {
                        return new ReadOnlyStringWrapper(arg0.getValue().getCost() + " €");
                    }
                });
        totals.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<OrderLine, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<OrderLine, String> arg0) {
                        return new ReadOnlyStringWrapper(
                                (arg0.getValue().getQuantity() * arg0.getValue().getCost()) + " €");
                    }
                });
    }

    public void refresh() {
        MainWindowController.runAsynchronously(() -> {
            order = MainWindowController.factory.getOrderDAO().getById(order.getId());
            var customerModel = MainWindowController.factory.getCustomerDAO().getById(order.getCustomer());
            var lines = MainWindowController.factory.getOrderLineDAO().getAllFromOrder(order.getId());
            allProducts.clear();

            for (Product prod : MainWindowController.factory.getProductDAO().getAll())
                allProducts.put(prod.getId(), prod);
            return new Pair<>(customerModel, lines);
        }, (pair) -> {
            tab.setText("Détail:" + pair.getKey().getSurname() + " "
                    + order.getDate().format(DateTimeFormatter.ofPattern("dd/MM")));
            id.setText("ID : " + order.getId());
            customer.setText(pair.getKey().getName() + " " + pair.getKey().getSurname());
            customer.setOnAction((e) -> {
                MainWindowController.detailCustomer(pair.getKey());
            });
            date.setText("Date : " + order.getDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
            time.setText("Heure : " + order.getDate().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            double orderCost = 0;
            displayedLines.clear();
            displayedLines.addAll(pair.getValue());
            for (OrderLine orderLine : pair.getValue()) {
                orderCost += orderLine.getQuantity() * orderLine.getCost();
            }
            totalCost.setText("Coût total : " + orderCost + " €");
        });
        id.setText("ID : " + order.getId());

    }

    public void edit() {
        MainWindowController.editOrder(order, this);
    }

    public void remove() {
        MainWindowController.removeOrder(order, () -> {
            EventHandler<Event> handler = tab.getOnClosed();
            if (null != handler) {
                handler.handle(null);
            } else {
                tab.getTabPane().getTabs().remove(tab);
            }
        }, null);
    }
}
