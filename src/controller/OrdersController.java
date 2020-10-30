package controller;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import model.Customer;
import model.Order;
import model.OrderLine;
import model.Product;

@SuppressWarnings("unchecked")
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
    public Label displayCount;
    @FXML
    public BorderPane mainPane;
    @FXML
    public ToggleButton filtersButton;
    @FXML
    public Node filterPanel;
    @FXML
    public VBox customerVBox;
    @FXML
    public VBox dateVBox;
    @FXML
    public VBox costVBox;
    @FXML
    public VBox productVBox;

    private List<Order> allItems;
    private Map<Integer, Customer> allCusts;
    private Map<Integer, Product> allProds;
    private Map<Order, OrderLine[]> allLines;
    private ObservableList<TreeItem<TableLine>> displayedItems;
    private ObservableList<Customer> custFilterList;
    private ObservableList<Product> prodFilterList;
    private List<Predicate<Order>> customerFilters;
    private List<Predicate<Order>> dateFilters;
    private List<Predicate<Order>> costFilters;
    private List<Predicate<Order>> productFilters;

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

    public static Tab createControl(Customer customerToDisplay) {
        try {
            URL fxmlURL = ProductsController.class.getResource("../view/Orders.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            var tab = fxmlLoader.<TabPane>load();
            var controller = fxmlLoader.<OrdersController>getController();
            var combo = (ComboBox<Customer>) ((HBox) controller.customerVBox.getChildren().get(0)).getChildren().get(0);
            combo.getSelectionModel().select(customerToDisplay);
            controller.filter();
            return tab.getTabs().get(0);
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
        customerFilters = new LinkedList<Predicate<Order>>();
        dateFilters = new LinkedList<Predicate<Order>>();
        costFilters = new LinkedList<Predicate<Order>>();
        productFilters = new LinkedList<Predicate<Order>>();
        custFilterList = FXCollections.observableList(new LinkedList<Customer>());
        prodFilterList = FXCollections.observableList(new LinkedList<Product>());
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
                            TableLine element = table.getTreeItem(getIndex()).getValue();
                            MainWindowController.removeOrder(element.order, () -> {
                                allItems.remove(element.order);
                                if (allItems.size() > 0) {
                                    double min = getOrderCost(allItems.get(0));
                                    double max = getOrderCost(allItems.get(0));
                                    for (Order order : allItems) {
                                        var cost = getOrderCost(order);
                                        if (min > cost)
                                            min = cost;
                                        if (max < cost)
                                            max = cost;
                                    }
                                    for (Node node : costVBox.getChildren()) {
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
                                } else
                                    for (Node node : costVBox.getChildren()) {
                                        var sliderMin = (Slider) ((VBox) node).getChildren().get(0);
                                        var sliderMax = (Slider) ((VBox) node).getChildren().get(1);
                                        sliderMin.setValue(0);
                                        sliderMin.setValue(0);
                                        sliderMax.setValue(0);
                                        sliderMax.setValue(0);
                                        sliderMin.setMin(0);
                                        sliderMin.setMax(0);
                                        sliderMax.setMin(0);
                                        sliderMax.setMax(0);
                                    }
                                applyFilters();
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

        var root = new TreeItem<TableLine>();
        displayedItems = root.getChildren();
        allItems = new LinkedList<Order>();
        allCusts = new HashMap<Integer, Customer>();
        allLines = new HashMap<Order, OrderLine[]>();
        allProds = new HashMap<Integer, Product>();
        table.setRoot(root);
        table.setShowRoot(false);
        table.setOnKeyPressed((KeyEvent ev) -> {
            if (ev.getCode() == KeyCode.DELETE) {
                var ord = table.getSelectionModel().getSelectedItem();
                if (ord != null && ord.getValue().order != null) {
                    MainWindowController.removeOrder(ord.getValue().order, () -> {
                        allItems.remove(ord.getValue().order);
                        if (allItems.size() > 0) {
                            double min = getOrderCost(allItems.get(0));
                            double max = getOrderCost(allItems.get(0));
                            for (Order order : allItems) {
                                var cost = getOrderCost(order);
                                if (min > cost)
                                    min = cost;
                                if (max < cost)
                                    max = cost;
                            }
                            for (Node node : costVBox.getChildren()) {
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
                        } else
                            for (Node node : costVBox.getChildren()) {
                                var sliderMin = (Slider) ((VBox) node).getChildren().get(0);
                                var sliderMax = (Slider) ((VBox) node).getChildren().get(1);
                                sliderMin.setValue(0);
                                sliderMin.setValue(0);
                                sliderMax.setValue(0);
                                sliderMax.setValue(0);
                                sliderMin.setMin(0);
                                sliderMin.setMax(0);
                                sliderMax.setMin(0);
                                sliderMax.setMax(0);
                            }
                        applyFilters();
                    }, null);
                }
            }
        });
        addCustomerFilter();
        addDateFilter();
        addCostFilter();
        addProductFilter();
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
        }, () -> {
            custFilterList.clear();
            custFilterList.addAll(allCusts.values());
            prodFilterList.clear();
            prodFilterList.addAll(allProds.values());
            for (Node node : customerVBox.getChildren()) {
                var box = (ComboBox<Customer>) ((HBox) node).getChildren().get(0);
                box.getSelectionModel().select((Customer) box.getUserData());
            }
            for (Node node : productVBox.getChildren()) {
                var box = (ComboBox<Product>) ((HBox) node).getChildren().get(0);
                box.getSelectionModel().select((Product) box.getUserData());
            }
            if (allItems.size() > 0) {
                double min = getOrderCost(allItems.get(0));
                double max = getOrderCost(allItems.get(0));
                for (Order order : allItems) {
                    var cost = getOrderCost(order);
                    if (min > cost)
                        min = cost;
                    if (max < cost)
                        max = cost;
                }
                for (Node node : costVBox.getChildren()) {
                    var sliderMin = (Slider) ((VBox) node).getChildren().get(0);
                    var sliderMax = (Slider) ((VBox) node).getChildren().get(1);
                    boolean resetMax = sliderMax.getValue() < .01;
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
                    if (resetMax)
                        sliderMax.setValue(max);
                }
            } else
                for (Node node : costVBox.getChildren()) {
                    var sliderMin = (Slider) ((VBox) node).getChildren().get(0);
                    var sliderMax = (Slider) ((VBox) node).getChildren().get(1);
                    sliderMin.setValue(0);
                    sliderMin.setValue(0);
                    sliderMax.setValue(0);
                    sliderMax.setValue(0);
                    sliderMin.setMin(0);
                    sliderMin.setMax(0);
                    sliderMax.setMin(0);
                    sliderMax.setMax(0);
                }
            applyFilters();
        });

    }

    public void applyFilters() {
        displayedItems.clear();
        for (Order ord : allItems) {
            if (Utilities.testAny(customerFilters, (o) -> o.test(ord), true)
                    && Utilities.testAny(costFilters, (o) -> o.test(ord), true)
                    && Utilities.testAny(dateFilters, (o) -> o.test(ord), true)
                    && Utilities.testAny(productFilters, (o) -> o.test(ord), true)) {
                var item = new TreeItem<TableLine>(new TableLine(ord));
                displayedItems.add(item);
                for (OrderLine line : allLines.get(ord))
                    item.getChildren().add(new TreeItem<TableLine>(new TableLine(line)));
            }
        }
        displayCount.setText(displayedItems.size() + " / " + allItems.size() + " affiché"
                + (displayedItems.size() != 1 ? "s " : " "));
    }

    public double getOrderCost(Order ord) {
        double res = 0;
        for (OrderLine line : allLines.get(ord))
            res += line.getCost() * line.getQuantity();
        return res;
    }

    public void filter() {
        customerFilters.clear();
        dateFilters.clear();
        costFilters.clear();
        productFilters.clear();
        for (Node filter : customerVBox.getChildren()) {
            var box = (ComboBox<Customer>) ((HBox) filter).getChildren().get(0);
            var cust = box.getSelectionModel().getSelectedItem();
            if (cust != null)
                customerFilters.add((ord) -> ord.getCustomer() == cust.getId());
        }
        for (Node node : costVBox.getChildren()) {
            var sliderMin = (Slider) ((VBox) node).getChildren().get(0);
            var sliderMax = (Slider) ((VBox) node).getChildren().get(1);
            costFilters.add(
                    (ord) -> getOrderCost(ord) >= sliderMin.getValue() && getOrderCost(ord) <= sliderMax.getValue());
        }
        for (Node filter : productVBox.getChildren()) {
            var box = (ComboBox<Product>) ((HBox) filter).getChildren().get(0);
            var prod = box.getSelectionModel().getSelectedItem();
            if (prod != null)
                productFilters.add((ord) -> Utilities.testAny(allLines.get(ord),
                        (line) -> line.getProduct() == prod.getId(), false));
        }
        for (Node node : dateVBox.getChildren()) {
            var minPicker = (DatePicker) ((HBox) ((VBox) node).getChildren().get(0)).getChildren().get(1);
            var maxPicker = (DatePicker) ((HBox) ((VBox) node).getChildren().get(1)).getChildren().get(1);
            if (minPicker.getValue() != null || maxPicker.getValue() != null)
                if (minPicker.getValue() == null)
                    dateFilters.add((ord) -> !ord.getDate().toLocalDate().isAfter(maxPicker.getValue()));
                else if (maxPicker.getValue() == null)
                    dateFilters.add((ord) -> !ord.getDate().toLocalDate().isBefore(minPicker.getValue()));
                else
                    dateFilters.add((ord) -> !ord.getDate().toLocalDate().isAfter(maxPicker.getValue())
                            && !ord.getDate().toLocalDate().isBefore(minPicker.getValue()));
        }
        applyFilters();
    }

    public void addCustomerFilter() {
        var custs = new ComboBox<Customer>();
        custs.setItems(custFilterList);
        custs.setOnAction((e) -> {
            if (custs.getSelectionModel().getSelectedIndex() != -1)
                custs.setUserData(custs.getSelectionModel().getSelectedItem());
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
        deleteButton.setOnAction((e) -> customerVBox.getChildren().remove(box));
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(custs, deleteButton);
        HBox.setMargin(custs, new Insets(5));
        HBox.setMargin(deleteButton, new Insets(5));
        customerVBox.getChildren().add(box);
    }

    public void addDateFilter() {
        var pickerMin = new DatePicker();
        var pickerMax = new DatePicker();
        pickerMin.getEditor().focusedProperty().addListener((a1, a2, value) -> {
            if (!value) {
                if (pickerMin.getEditor().getText().length() == 0)
                    pickerMin.setValue(null);
                else {
                    try {
                        pickerMin.setValue(pickerMin.getConverter().fromString(pickerMin.getEditor().getText()));
                    } catch (DateTimeParseException e) {
                    }
                }
            }
        });
        pickerMax.getEditor().focusedProperty().addListener((a1, a2, value) -> {
            if (!value) {
                if (pickerMax.getEditor().getText().length() == 0)
                    pickerMax.setValue(null);
                else {
                    try {
                        pickerMax.setValue(pickerMax.getConverter().fromString(pickerMax.getEditor().getText()));
                    } catch (DateTimeParseException e) {
                    }
                }
            }
        });
        pickerMin.getEditor().textProperty().addListener((a1, a2, value) -> pickerMin.setStyle(
                Utilities.validDateFormat(value, pickerMin.getConverter()) ? null : "-fx-border-color: red;"));
        pickerMax.getEditor().textProperty().addListener((a1, a2, value) -> pickerMax.setStyle(
                Utilities.validDateFormat(value, pickerMax.getConverter()) ? null : "-fx-border-color: red;"));
        var deleteButton = new Button();
        var img = new ImageView(MainWindowController.removeImage);
        img.setPreserveRatio(false);
        img.setSmooth(false);
        img.setFitHeight(24);
        img.setFitWidth(24);
        deleteButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        deleteButton.setGraphic(img);
        var mainBox = new VBox();
        deleteButton.setOnAction((e) -> dateVBox.getChildren().remove(mainBox));
        var minBox = new HBox();
        mainBox.getChildren().add(minBox);
        minBox.getChildren().add(new Label("Après :"));
        minBox.getChildren().add(pickerMin);
        HBox.setMargin(pickerMin, new Insets(5));
        minBox.setAlignment(Pos.CENTER_LEFT);
        var maxBox = new HBox();
        mainBox.getChildren().add(maxBox);
        maxBox.getChildren().add(new Label("Avant :"));
        maxBox.getChildren().add(pickerMax);
        HBox.setMargin(pickerMax, new Insets(5));
        maxBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.getChildren().add(deleteButton);
        VBox.setMargin(deleteButton, new Insets(5));
        VBox.setMargin(minBox, new Insets(5));
        VBox.setMargin(maxBox, new Insets(5));
        mainBox.getChildren().add(new Separator(Orientation.HORIZONTAL));
        dateVBox.getChildren().add(mainBox);
    }

    public void addCostFilter() {
        var value = new Label();
        var deleteButton = new Button();
        var img = new ImageView(MainWindowController.removeImage);
        img.setPreserveRatio(false);
        img.setSmooth(false);
        img.setFitHeight(24);
        img.setFitWidth(24);
        deleteButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        deleteButton.setGraphic(img);
        double min = allItems.size() > 0 ? getOrderCost(allItems.get(0)) : 0;
        double max = allItems.size() > 0 ? getOrderCost(allItems.get(0)) : 0;
        for (Order order : allItems) {
            var cost = getOrderCost(order);
            if (min > cost)
                min = cost;
            if (max < cost)
                max = cost;
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
        deleteButton.setOnAction((e) -> costVBox.getChildren().remove(box));
        {
            var detailBox = new HBox();
            box.getChildren().add(detailBox);
            HBox.setMargin(value, new Insets(5));
            HBox.setMargin(deleteButton, new Insets(5));
            detailBox.getChildren().addAll(value, deleteButton);
        }
        box.getChildren().add(new Separator(Orientation.HORIZONTAL));
        Runnable update = () -> {
            if (Double.valueOf(sliderMin.getValue()).equals(Double.NaN))
                sliderMin.setValue(sliderMin.getMin());
            if (Double.valueOf(sliderMax.getValue()).equals(Double.NaN))
                sliderMax.setValue(sliderMax.getMax());
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
        costVBox.getChildren().add(box);
    }

    public void addProductFilter() {
        var prods = new ComboBox<Product>();
        prods.setItems(prodFilterList);
        prods.setOnAction((e) -> {
            if (prods.getSelectionModel().getSelectedIndex() != -1)
                prods.setUserData(prods.getSelectionModel().getSelectedItem());
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
        deleteButton.setOnAction((e) -> productVBox.getChildren().remove(box));
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(prods, deleteButton);
        HBox.setMargin(prods, new Insets(5));
        HBox.setMargin(deleteButton, new Insets(5));
        productVBox.getChildren().add(box);
    }
}