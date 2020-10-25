package controller;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import model.Customer;
import model.Order;
import model.OrderLine;
import model.Product;

public class OrdersController implements Initializable {
    private class TableLine {
        public Order order;
        public OrderLine line;

        public TableLine(Order o) {
            order = o;
            line = null;
        }

        public TableLine(OrderLine o) {
            order = null;
            line = o;
        }
    }

    @FXML
    public TreeTableView<TableLine> table;
    @FXML
    public TreeTableColumn<TableLine, String> customer;
    @FXML
    public TreeTableColumn<TableLine, String> id;
    @FXML
    public TreeTableColumn<TableLine, String> date;
    @FXML
    public TreeTableColumn<TableLine, Order> detail;
    @FXML
    public TreeTableColumn<TableLine, Order> remove;
    @FXML
    public TreeTableColumn<TableLine, String> products;
    @FXML
    public TreeTableColumn<TableLine, String> quantities;
    @FXML
    public TreeTableColumn<TableLine, String> costs;
    @FXML
    public TreeTableColumn<TableLine, String> totals;
    @FXML
    private List<Order> allItems;
    private Map<Integer, Customer> allCusts;
    private Map<Integer, Product> allProds;
    private Map<Order, OrderLine[]> allLines;
    private TreeItem<TableLine> displayedItems;

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

        customer.setCellFactory(new Callback<TreeTableColumn<TableLine, String>, TreeTableCell<TableLine, String>>() {
            @Override
            public TreeTableCell<TableLine, String> call(TreeTableColumn<TableLine, String> arg0) {
                return new TreeTableCell<TableLine, String>() {

                    private Hyperlink link = new Hyperlink();

                    {
                        link.setOnAction((event) -> {
                            TableLine element = table.getTreeItem(getIndex()).getValue();
                            MainWindowController.detailCustomer(allCusts.get(element.order.getCustomer()));
                        });
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        link.setText(item);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            setGraphic(link);
                        }
                    }
                };
            };
        });
        customer.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<TableLine, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<TableLine, String> arg0) {
                        if (arg0.getValue().getValue().order == null)
                            return null;
                        var cust = allCusts.get(arg0.getValue().getValue().order.getCustomer());
                        return new ReadOnlyStringWrapper(cust.getName() + " " + cust.getSurname());
                    }
                });
        date.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<TableLine, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<TableLine, String> arg0) {
                        if (arg0.getValue().getValue().order == null)
                            return null;
                        return new ReadOnlyStringWrapper(arg0.getValue().getValue().order.getDate()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm")));
                    }
                });
        id.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<TableLine, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<TableLine, String> arg0) {
                        if (arg0.getValue().getValue().order == null)
                            return null;
                        return new ReadOnlyStringWrapper(Integer.toString(arg0.getValue().getValue().order.getId()));
                    }
                });
        detail.setCellFactory(new Callback<TreeTableColumn<TableLine, Order>, TreeTableCell<TableLine, Order>>() {
            @Override
            public TreeTableCell<TableLine, Order> call(TreeTableColumn<TableLine, Order> arg0) {
                return new TreeTableCell<TableLine, Order>() {

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
                    public void updateItem(Order item, boolean empty) {
                        button.setOnAction((ActionEvent event) -> {
                            MainWindowController.detailOrder(item);
                        });
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            };
        });
        detail.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<TableLine, Order>, ObservableValue<Order>>() {

                    @Override
                    public ObservableValue<Order> call(CellDataFeatures<TableLine, Order> arg0) {
                        if (arg0.getValue().getValue().order == null)
                            return null;
                        return new ReadOnlyObjectWrapper<Order>(arg0.getValue().getValue().order);
                    }
                });
        remove.setCellFactory(new Callback<TreeTableColumn<TableLine, Order>, TreeTableCell<TableLine, Order>>() {
            @Override
            public TreeTableCell<TableLine, Order> call(TreeTableColumn<TableLine, Order> arg0) {
                return new TreeTableCell<TableLine, Order>() {

                    private Button button = new Button();
                    {
                        var iv = new ImageView();
                        iv.setSmooth(false);
                        iv.setPreserveRatio(false);
                        iv.setImage(MainWindowController.removeImage);
                        button.setGraphic(iv);
                        alignmentProperty().set(Pos.BASELINE_CENTER);
                        button.setOnAction((ActionEvent event) -> {
                            TableLine element;
                            {
                                var cell = table.getEditingCell();
                                if (cell != null)
                                    element = cell.getTreeItem().getValue();
                                else
                                    return;
                            }
                            MainWindowController.removeOrder(element.order, () -> {
                                allItems.remove(element.order);
                                applySearchKey();
                            }, null);
                        });
                    }

                    @Override
                    public void updateItem(Order item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            };
        });
        remove.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<TableLine, Order>, ObservableValue<Order>>() {

                    @Override
                    public ObservableValue<Order> call(CellDataFeatures<TableLine, Order> arg0) {
                        if (arg0.getValue().getValue().order == null)
                            return null;
                        return new ReadOnlyObjectWrapper<Order>(arg0.getValue().getValue().order);
                    }
                });
        totals.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<TableLine, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<TableLine, String> arg0) {
                        if (arg0.getValue().getValue().order == null) {
                            var line = arg0.getValue().getValue().line;
                            return new ReadOnlyStringWrapper(
                                    Double.toString(line.getCost() * line.getQuantity()) + " €");
                        } else
                            return new ReadOnlyStringWrapper(
                                    Double.toString(getOrderCost(arg0.getValue().getValue().order)) + " €");
                    }
                });
        products.setCellFactory(new Callback<TreeTableColumn<TableLine, String>, TreeTableCell<TableLine, String>>() {
            @Override
            public TreeTableCell<TableLine, String> call(TreeTableColumn<TableLine, String> arg0) {
                return new TreeTableCell<TableLine, String>() {

                    private Hyperlink link = new Hyperlink();

                    {
                        link.setOnAction((event) -> {
                            TableLine element = table.getTreeItem(getIndex()).getValue();
                            MainWindowController.detailProduct(allProds.get(element.line.getProduct()));
                        });
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        link.setText(item);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            setGraphic(link);
                        }
                    }
                };
            };
        });
        products.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<TableLine, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<TableLine, String> arg0) {
                        if (arg0.getValue().getValue().line == null)
                            return null;
                        var prod = allProds.get(arg0.getValue().getValue().line.getProduct());
                        return new ReadOnlyStringWrapper(prod.getName());
                    }
                });
        quantities.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<TableLine, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<TableLine, String> arg0) {
                        if (arg0.getValue().getValue().line == null)
                            return null;
                        return new ReadOnlyStringWrapper(
                                Integer.toString(arg0.getValue().getValue().line.getQuantity()));
                    }
                });
        costs.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<TableLine, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<TableLine, String> arg0) {
                        if (arg0.getValue().getValue().line == null)
                            return null;
                        return new ReadOnlyStringWrapper(
                                Double.toString(arg0.getValue().getValue().line.getCost()) + " €");
                    }
                });

        displayedItems = new TreeItem<TableLine>();
        allItems = new LinkedList<Order>();
        allCusts = new HashMap<Integer, Customer>();
        allLines = new HashMap<Order, OrderLine[]>();
        allProds = new HashMap<Integer, Product>();
        table.setRoot(displayedItems);
        table.setShowRoot(false);
        table.setOnKeyPressed((KeyEvent ev) -> {
            if (ev.getCode() == KeyCode.DELETE) {
                var ord = table.getSelectionModel().getSelectedItem();
                if (ord != null && ord.getValue().order != null) {
                    MainWindowController.removeOrder(ord.getValue().order, () -> {
                        allItems.remove(ord.getValue().order);
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
            allCusts.clear();
            allLines.clear();
            allProds.clear();
            for (Order ord : MainWindowController.factory.getOrderDAO().getAll()) {
                allItems.add(ord);
                allLines.put(ord, MainWindowController.factory.getOrderLineDAO().getAllFromOrder(ord.getId()));
            }
            for (Customer cust : MainWindowController.factory.getCustomerDAO().getAll())
                allCusts.put(cust.getId(), cust);
            for (Product prod : MainWindowController.factory.getProductDAO().getAll())
                allProds.put(prod.getId(), prod);
        }, () -> applySearchKey());

    }

    public void applySearchKey() {
        displayedItems.getChildren().clear();
        for (Order ord : allItems) {
            boolean toAdd = false;
            if (toAdd || true) {
                var item = new TreeItem<TableLine>(new TableLine(ord));
                displayedItems.getChildren().add(item);
                for (OrderLine line : allLines.get(ord))
                    item.getChildren().add(new TreeItem<TableLine>(new TableLine(line)));
            }
        }
    }

    public double getOrderCost(Order ord) {
        double res = 0;
        for (OrderLine line : allLines.get(ord))
            res += line.getCost() * line.getQuantity();
        return res;
    }
}