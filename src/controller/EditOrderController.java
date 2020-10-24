package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import dao.DAOException;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import model.Category;
import model.Customer;
import model.Order;
import model.OrderLine;
import model.Product;
import view.Customers;

public class EditOrderController implements Initializable {
    @FXML
    public ComboBox<Customer> customer;
    @FXML
    public Hyperlink customerLink;
    @FXML
    public DatePicker date;
    @FXML
    public Spinner<Integer> hours;
    @FXML
    public Spinner<Integer> minutes;
    @FXML
    public Spinner<Integer> seconds;
    @FXML
    public TableView<OrderLine> table;
    @FXML
    public TableColumn<OrderLine, String> products;
    @FXML
    public TableColumn<OrderLine, String> quantities;
    @FXML
    public TableColumn<OrderLine, String> prices;
    @FXML
    public TableColumn<OrderLine, Void> deletes;
    @FXML
    public ComboBox<Product> availableProducts;
    @FXML
    public Button addProduct;
    @FXML
    public Tab tab;
    public Order order;
    public OrderDetailController detailController;
    public Customer initCustomer;
    public boolean reopenDetails;
    public boolean saved;
    public ObservableList<Customer> customers;
    public Map<Integer, Product> allProducts;
    public List<OrderLine> initialValues;
    private Comparator<Product> productComparator = new Comparator<>() {
        @Override
        public int compare(Product o1, Product o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    public static EditOrderController createController(Order ord, OrderDetailController detail) {
        try {
            URL fxmlURL = EditOrderController.class.getResource("../view/EditOrder.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            var pane = fxmlLoader.<TabPane>load();
            var controller = fxmlLoader.<EditOrderController>getController();
            controller.tab = pane.getTabs().get(0);
            controller.setupFields(ord);
            controller.detailController = detail;
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    public void scanAvailableProducts() {
        availableProducts.getItems().clear();
        for (var product : allProducts.values()) {
            boolean add = true;
            for (var orderLine : table.getItems())
                if (orderLine.getProduct() == product.getId()) {
                    add = false;
                    break;
                }
            if (add)
                availableProducts.getItems().add(product);
        }
        availableProducts.getSelectionModel().select(-1);
        addProduct.setDisable(true);
    }

    public void refreshAvailableProducts() {
        MainWindowController.runAsynchronously(() -> {
            allProducts.clear();
            for (var product : MainWindowController.factory.getProductDAO().getAll())
                allProducts.put(product.getId(), product);
            Platform.runLater(() -> scanAvailableProducts());
        });
    }

    public void setupFields(Order ord) {
        order = ord;
        MainWindowController.runAsynchronously(() -> {
            for (Product prod : MainWindowController.factory.getProductDAO().getAll())
                allProducts.put(prod.getId(), prod);
            initialValues = new LinkedList<>();
            for (OrderLine line : MainWindowController.factory.getOrderLineDAO().getAllFromOrder(ord.getId()))
                initialValues.add(line.clone());
            initCustomer = MainWindowController.factory.getCustomerDAO().getById(order.getCustomer());
            var list = MainWindowController.factory.getCustomerDAO().getAll();
            Platform.runLater(() -> {
                customers.addAll(list);
                refreshAvailableProducts();
                reset();
            });
        });
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        reopenDetails = false;
        customers = FXCollections.observableList(new LinkedList<Customer>());
        allProducts = new HashMap<>();
        customer.setItems(customers);
        customerLink.setOnAction((ActionEvent) -> {
            MainWindowController.detailCustomer(customer.getSelectionModel().getSelectedItem());
        });
        hours.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0, 1));
        minutes.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 1));
        seconds.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 1));
        products.setCellFactory(new Callback<TableColumn<OrderLine, String>, TableCell<OrderLine, String>>() {
            @Override
            public TableCell<OrderLine, String> call(TableColumn<OrderLine, String> arg0) {
                return new TableCell<OrderLine, String>() {

                    private Hyperlink link = new Hyperlink();

                    {
                        link.setOnAction((e) -> {
                            MainWindowController
                                    .detailProduct(allProducts.get(table.getItems().get(getIndex()).getProduct()));
                        });
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
        quantities.setCellFactory(new Callback<TableColumn<OrderLine, String>, TableCell<OrderLine, String>>() {
            @Override
            public TableCell<OrderLine, String> call(TableColumn<OrderLine, String> arg0) {
                return new TableCell<OrderLine, String>() {

                    private Spinner<Integer> value = new Spinner<>();

                    {
                        alignmentProperty().set(Pos.CENTER_LEFT);
                        value.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1, 1));
                        value.setEditable(true);
                        value.getValueFactory().valueProperty().addListener(new ChangeListener<Integer>() {
                            @Override
                            public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
                                if (table.getItems().size() > getIndex() && getIndex() != -1)
                                    table.getItems().get(getIndex()).setQuantity(arg2);
                            }
                        });
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && item.length() > 0)
                            value.getValueFactory().setValue(Integer.parseInt(item));
                        else
                            value.getValueFactory().setValue(1);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(value);
                        }
                    }
                };
            };
        });
        prices.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<OrderLine, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<OrderLine, String> arg0) {
                        return new ReadOnlyStringWrapper(Double.toString(arg0.getValue().getCost()));
                    }
                });
        prices.setCellFactory(new Callback<TableColumn<OrderLine, String>, TableCell<OrderLine, String>>() {
            @Override
            public TableCell<OrderLine, String> call(TableColumn<OrderLine, String> arg0) {
                return new TableCell<OrderLine, String>() {

                    private Spinner<Double> value = new Spinner<>();
                    private Node graphic;

                    {
                        value.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(.01, 99, 1, 1));
                        value.setEditable(true);
                        value.setPrefWidth(75);
                        value.getValueFactory().valueProperty().addListener(new ChangeListener<Double>() {
                            @Override
                            public void changed(ObservableValue<? extends Double> arg0, Double arg1, Double arg2) {
                                if (table.getItems().size() > getIndex() && getIndex() != -1)
                                    table.getItems().get(getIndex()).setCost(arg2);
                            }
                        });
                        var box = new HBox();
                        box.setAlignment(Pos.CENTER_LEFT);
                        box.getChildren().add(value);
                        box.getChildren().add(new Label(" €"));
                        graphic = box;
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && item.length() > 0)
                            value.getValueFactory().setValue(Double.parseDouble(item));
                        else
                            value.getValueFactory().setValue(1.);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(graphic);
                        }
                    }
                };
            };
        });
        deletes.setCellFactory(new Callback<TableColumn<OrderLine, Void>, TableCell<OrderLine, Void>>() {
            @Override
            public TableCell<OrderLine, Void> call(TableColumn<OrderLine, Void> arg0) {
                return new TableCell<OrderLine, Void>() {

                    private Button delButton = new Button();

                    {
                        delButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        var img = new ImageView();
                        img.setFitWidth(24);
                        img.setFitHeight(24);
                        img.setPreserveRatio(false);
                        img.setSmooth(false);
                        img.setImage(MainWindowController.removeImage);
                        delButton.setGraphic(img);
                        delButton.setOnAction((e) -> {
                            availableProducts.getItems()
                                    .add(allProducts.get(table.getItems().get(getIndex()).getProduct()));
                            table.getItems().remove(getIndex());
                            FXCollections.sort(availableProducts.getItems(), productComparator);
                        });
                        alignmentProperty().set(Pos.BASELINE_CENTER);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(delButton);
                        }
                    }
                };
            };
        });
        availableProducts.setOnAction((e) -> {
            addProduct.setDisable(availableProducts.getSelectionModel().getSelectedIndex() == -1);
        });
        addProduct.setOnAction((e) -> {
            table.getItems()
                    .add(new OrderLine(order.getId(), availableProducts.getSelectionModel().getSelectedItem().getId(),
                            availableProducts.getSelectionModel().getSelectedItem().getCost(), 1));
            availableProducts.getItems().remove(availableProducts.getSelectionModel().getSelectedIndex());
            FXCollections.sort(availableProducts.getItems(), productComparator);
            availableProducts.getSelectionModel().select(-1);
            addProduct.setDisable(true);
        });
    }

    public void refreshCust() {
        MainWindowController.runAsynchronously(() -> {
            var list = MainWindowController.factory.getCustomerDAO().getAll();
            Platform.runLater(() -> {
                var c = customer.getSelectionModel().getSelectedItem();
                customers.clear();
                customers.addAll(list);
                if (c != null) {
                    customer.getSelectionModel().select(c);
                    customerLink.setDisable(false);
                } else
                    customerLink.setDisable(true);
            });
        });
    }

    public void reset() {
        customer.getSelectionModel().select(null);
        for (Customer cust : customers)
            if (cust.getId() == order.getCustomer()) {
                customer.getSelectionModel().select(cust);
                break;
            }
        date.setValue(order.getDate().toLocalDate());
        hours.getEditor().setText("" + order.getDate().getHour());
        minutes.getEditor().setText("" + order.getDate().getMinute());
        seconds.getEditor().setText("" + order.getDate().getSecond());
        tab.setText("Éditer:" + initCustomer.getSurname() + " "
                + order.getDate().format(DateTimeFormatter.ofPattern("dd/MM")));
        customer.getSelectionModel().select(initCustomer);
        var prods = table.getItems();
        prods.clear();
        for (OrderLine orderLine : initialValues)
            prods.add(orderLine.clone());
        scanAvailableProducts();
    }

    public void cancel() {
        saved = false;
        reopenDetails = true;
        tab.getOnClosed().handle(null);
    }

    public void dateNow() {
        date.setValue(LocalDate.now());
        hours.getValueFactory().setValue(LocalDateTime.now().getHour());
        minutes.getValueFactory().setValue(LocalDateTime.now().getMinute());
        seconds.getValueFactory().setValue(LocalDateTime.now().getSecond());
    }

    public void save() {
        // if (category.getSelectionModel().getSelectedItem() == null) {
        // new Alert(AlertType.WARNING, "Selectionnez une catégorie").showAndWait();
        // return;
        // }
        // if (cost.getText().length() == 0 || Double.parseDouble(cost.getText()) <= 0)
        // {
        // new Alert(AlertType.WARNING, "Selectionnez un prix supérieur à
        // zéro").showAndWait();
        // return;
        // }
        // if (name.getText().length() == 0) {
        // new Alert(AlertType.WARNING, "Entrez un nom").showAndWait();
        // return;
        // }
        // product.setCategory(category.getSelectionModel().getSelectedItem().getId());
        // product.setCost(Double.parseDouble(cost.getText()));
        // product.setDescription(description.getText());
        // product.setImagePath(image.getText());
        // product.setName(name.getText());
        // try {
        // if (MainWindowController.factory.getProductDAO().update(product)) {
        // saved = true;
        // reopenDetails = true;
        // tab.getOnClosed().handle(null);
        // } else
        // new Alert(AlertType.ERROR, "Impossible de modifier ce
        // produit").showAndWait();

        // } catch (DAOException e) {
        // new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
        // }
    }
}