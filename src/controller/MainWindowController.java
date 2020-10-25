package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.DAOException;
import dao.DAOFactory;
import dao.DAOFactory.Mode;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Category;
import model.Customer;
import model.Order;
import model.Product;

public class MainWindowController implements Initializable {
    public static Stage window;

    public static final Image detailImage = new Image("assets/icons/detail.png");
    public static final Image removeImage = new Image("assets/icons/remove.png");
    @FXML
    public TabPane mainTabPane;
    @FXML
    public ProgressBar loading;
    @FXML
    public MenuButton connectionMode;
    @FXML
    public MenuButton location;
    private static TabPane tabInstance;
    private static ProgressBar loadingInstance;
    private static MainWindowController mainInstance;
    public static DAOFactory factory;
    private Mode DAOMode;
    private static ObservableList<MenuItem> locationMenu;
    private static int runningTasks;

    public static MainWindowController getInstance() {
        return mainInstance;
    }

    public static TabPane getMainTabPane() {
        return tabInstance;
    }

    public void close() {
        window.close();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        runningTasks = 0;
        tabInstance = mainTabPane;
        loadingInstance = loading;
        DAOMode = Mode.MEMORY;
        factory = DAOFactory.getFactory(DAOMode);
        connectionMode.setText("Mémoire");
        location.setText("Aucun onglet ouvert");
        locationMenu = location.getItems();
        mainTabPane.getTabs().addListener(new ListChangeListener<Tab>() {
            @Override
            public void onChanged(Change<? extends Tab> change) {
                while (change.next()) {
                    for (Tab tab : change.getAddedSubList()) {
                        var menuItem = new MenuItem(tab.getUserData().toString());
                        menuItem.setUserData(tab);
                        menuItem.setOnAction((e) -> tabInstance.getSelectionModel().select(tab));
                        locationMenu.add(menuItem);
                    }
                    for (Tab tab : change.getRemoved())
                        for (MenuItem menuItem : locationMenu)
                            if (menuItem.getUserData() == tab) {
                                locationMenu.remove(menuItem);
                                break;
                            }
                }
            }
        });
        mainTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> arg0, Tab arg1, Tab arg2) {
                if (arg2 != null)
                    location.setText(arg2.getUserData().toString());
                else
                    location.setText("Aucun onglet ouvert");
            }
        });
        window.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);

            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    if (mainTabPane.getTabs().size() > 0) {
                        Tab tab = mainTabPane.getSelectionModel().getSelectedItem();
                        EventHandler<Event> handler = tab.getOnClosed();
                        if (null != handler) {
                            handler.handle(null);
                        } else {
                            tab.getTabPane().getTabs().remove(tab);
                        }
                    } else
                        close();
                    ke.consume();
                }
            }
        });
    }

    public void addCateg() {
        addCategory();
    }

    public static void addCategory() {
        var tab = new Ptr<Tab>();
        runAsynchronously(() -> {
            tab.value = NewCategoryController.createControl();
            tab.value.setUserData("Catégories>Nouvelle");
        }, () -> {
            tabInstance.getTabs().add(tab.value);
            tabInstance.getSelectionModel().select(tab.value);
        });
    }

    public static void removeCategory(Category categ, Runnable deleted, Runnable notDeleted) {
        var alertType = new Ptr<AlertType>(AlertType.ERROR);
        var alertText = new Ptr<String>();
        runAsynchronously(() -> {
            try {
                var prods = factory.getProductDAO().getAll();
                for (Product product : prods) {
                    if (product.getCategory() == categ.getId()) {
                        alertText.value = "Un produit utilise cette catégorie";
                        alertType.value = AlertType.WARNING;
                        return;
                    }
                }
                if (!factory.getCategoryDAO().delete(categ))
                    alertText.value = "Une erreur est survenue";
            } catch (DAOException e) {
                alertText.value = e.getMessage();
            }
        }, () -> {
            if (alertText.value != null) {
                new Alert(alertType.value, alertText.value).showAndWait();
                if (notDeleted != null)
                    notDeleted.run();
            } else if (deleted != null)
                deleted.run();
        });

    }

    public static void addCustomer() {

    }

    public static void removeCustomer(Customer custo, Runnable deleted, Runnable notDeleted) {
        var alertType = new Ptr<AlertType>(AlertType.ERROR);
        var alertText = new Ptr<String>();
        runAsynchronously(() -> {
            try {
                var ords = factory.getOrderDAO().getAll();
                for (Order ord : ords) {
                    if (ord.getCustomer() == custo.getId()) {
                        alertText.value = "Une commande est associée à ce client";
                        alertType.value = AlertType.WARNING;
                        return;
                    }
                }

                if (!factory.getCustomerDAO().delete(custo))
                    alertText.value = "Une erreur est survenue";
            } catch (DAOException e) {
                alertText.value = e.getMessage();
            }
        }, () -> {
            if (alertText.value != null) {
                new Alert(alertType.value, alertText.value).showAndWait();
                if (notDeleted != null)
                    notDeleted.run();
            } else if (deleted != null)
                deleted.run();
        });
    }

    public void license() {
        try {
            URL fxmlURL = CategoriesController.class.getResource("../view/License.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            var tab = fxmlLoader.<TabPane>load().getTabs().get(0);
            tab.setUserData("Aide>Licence");
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void about() {

    }

    public static void addProduct() {
        runAsynchronously(new Runnable() {
            @Override
            public void run() {
                var tab = NewProductController.createControl();
                tab.setUserData("Produits>Nouveau");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                    }
                });
            }
        });

    }

    public void addCust() {
        addCustomer();
    }

    public void addProd() {
        addProduct();
    }

    public static void removeOrder(Order ord, Runnable deleted, Runnable notDeleted) {
        var alertType = new Ptr<AlertType>(AlertType.ERROR);
        var alertText = new Ptr<String>();
        runAsynchronously(() -> {
            try {
                if (!factory.getOrderDAO().delete(ord))
                    alertText.value = "Une erreur est survenue";
            } catch (

            DAOException e) {
                alertText.value = e.getMessage();
            }
        }, () -> {
            if (alertText.value != null) {
                new Alert(alertType.value, alertText.value).showAndWait();
                if (notDeleted != null)
                    notDeleted.run();
            } else if (deleted != null)
                deleted.run();
        });
    }

    public static void removeProduct(Product prod, Runnable deleted, Runnable notDeleted) {
        var alertType = new Ptr<AlertType>(AlertType.ERROR);
        var alertText = new Ptr<String>();
        runAsynchronously(() -> {
            try {
                if (factory.getOrderLineDAO().getAllFromProduct(prod.getId()).length != 0) {
                    alertText.value = "Une commande possède ce produit";
                    alertType.value = AlertType.WARNING;
                    if (notDeleted != null)
                        notDeleted.run();
                } else if (!factory.getProductDAO().delete(prod)) {
                    alertText.value = "Une erreur est survenue";
                }
            } catch (DAOException e) {
                alertText.value = e.getMessage();
            }
        }, () -> {
            if (alertText.value != null) {
                new Alert(alertType.value, alertText.value).showAndWait();
                if (notDeleted != null)
                    notDeleted.run();
            } else if (deleted != null)
                deleted.run();
        });
    }

    public void addOrd() {
        addOrder();
    }

    public static void addOrder() {
        runAsynchronously(new Runnable() {
            @Override
            public void run() {
                var tab = NewOrderController.createControl();
                tab.setUserData("Commandes>Nouvelle");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                    }
                });
            }
        });
    }

    public static void runAsynchronously(Runnable subthread) {
        runAsynchronously(subthread, null);
    }

    public static void runAsynchronously(Runnable subthread, Runnable mainthread) {
        Platform.runLater(() -> {
            newThread();
            new Thread(() -> {
                try {
                    subthread.run();
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        endThread();
                    });
                    throw e;
                }
                Platform.runLater(() -> {
                    try {
                        if (mainthread != null)
                            mainthread.run();
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            endThread();
                        });
                        throw e;
                    }
                    endThread();
                });
            }).start();
        });
    }

    public void seeCategs() {
        seeCategories();
    }

    public static void seeCategories() {
        runAsynchronously(new Runnable() {
            @Override
            public void run() {
                var tab = CategoriesController.createControl();
                tab.setUserData("Catégories>Tout voir");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                    }
                });
            }
        });
    }

    public void seeCusts() {
        seeCustomers();
    }

    public static void seeCustomers() {
        runAsynchronously(new Runnable() {
            @Override
            public void run() {
                var tab = CustomersController.createControl();
                tab.setUserData("Clients>Tout voir");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                    }
                });
            }
        });

    }

    public void seeProds() {
        seeProducts();
    }

    public static void seeProducts() {
        runAsynchronously(new Runnable() {
            @Override
            public void run() {
                var tab = ProductsController.createControl();
                tab.setUserData("Produits>Tout voir");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                    }
                });
            }
        });
    }

    public void seeOrds() {
        seeOrders();
    }

    public static void seeOrders() {
        runAsynchronously(new Runnable() {
            @Override
            public void run() {
                var tab = OrdersController.createControl();
                tab.setUserData("Commandes>Tout voir");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                    }
                });
            }
        });
    }

    public static void detailCategory(Category categ) {
        runAsynchronously(new Runnable() {
            @Override
            public void run() {
                var tab = CategoryDetailController.createControl(categ);
                tab.setUserData("Catégories>Détail");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                    }
                });
            }
        });
    }

    public static void detailCustomer(Customer cust) {

    }

    public static void detailProduct(Product prod) {
        runAsynchronously(new Runnable() {
            @Override
            public void run() {
                var tab = ProductDetailController.createControl(prod);
                tab.setUserData("Produits>Détail");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                    }
                });
            }
        });
    }

    public static void editProduct(Product prod, ProductDetailController controller) {
        runAsynchronously(new Runnable() {
            @Override
            public void run() {
                var editor = EditProductController.createController(prod, controller);
                editor.tab.setUserData("Produits>Éditer");
                editor.tab.setOnClosed((e1) -> Platform.runLater(() -> {
                    tabInstance.getTabs().remove(editor.tab);
                    if (editor.reopenDetails) {
                        tabInstance.getTabs().add(controller.tab);
                        tabInstance.getSelectionModel().select(controller.tab);
                        if (editor.saved)
                            controller.refresh();
                    }
                }));
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().remove(controller.tab);
                        tabInstance.getTabs().add(editor.tab);
                        tabInstance.getSelectionModel().select(editor.tab);
                    }
                });
            }
        });
    }

    public static void editCategory(Category categ, CategoryDetailController controller) {
        runAsynchronously(new Runnable() {

            @Override
            public void run() {
                var editor = EditCategoryController.createController(categ, controller);
                editor.tab.setUserData("Catégories>Éditer");
                editor.tab.setOnClosed((e1) -> Platform.runLater(() -> {
                    tabInstance.getTabs().remove(editor.tab);
                    if (editor.reopenDetails) {
                        tabInstance.getTabs().add(controller.tab);
                        tabInstance.getSelectionModel().select(controller.tab);
                        if (editor.saved)
                            controller.refresh();
                    }
                }));
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        tabInstance.getTabs().remove(controller.tab);
                        tabInstance.getTabs().add(editor.tab);
                        tabInstance.getSelectionModel().select(editor.tab);
                    }
                });
            }
        });
    }

    public static void editOrder(Order ord, OrderDetailController controller) {
        runAsynchronously(new Runnable() {
            @Override
            public void run() {
                var editor = EditOrderController.createController(ord, controller);
                editor.tab.setUserData("Commandes>Éditer");
                editor.tab.setOnClosed((e1) -> Platform.runLater(() -> {
                    tabInstance.getTabs().remove(editor.tab);
                    if (editor.reopenDetails) {
                        tabInstance.getTabs().add(controller.tab);
                        tabInstance.getSelectionModel().select(controller.tab);
                        if (editor.saved)
                            controller.refresh();
                    }
                }));
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().remove(controller.tab);
                        tabInstance.getTabs().add(editor.tab);
                        tabInstance.getSelectionModel().select(editor.tab);
                    }
                });
            }
        });
    }

    public static void detailOrder(Order ord) {
        runAsynchronously(new Runnable() {
            @Override
            public void run() {
                var tab = OrderDetailController.createControl(ord);
                tab.setUserData("Commandes>Détail");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                    }
                });
            }
        });
    }

    private static void newThread() {
        runningTasks++;
        scanThreads();
    }

    private static void scanThreads() {
        if (runningTasks == 0)
            loadingInstance.setProgress(0);
        else
            loadingInstance.setProgress(-1);
    }

    private static void endThread() {
        runningTasks--;
        scanThreads();
    }

    public void toMySql() {
        if (DAOMode != Mode.SQL) {
            if (mainTabPane.getTabs().size() > 0) {
                var dialog = new Alert(AlertType.WARNING,
                        "Changer de mode rendra obsolète tous les onglets ouverts, les fermer ?", ButtonType.YES,
                        ButtonType.NO, ButtonType.CANCEL);
                var res = dialog.showAndWait();
                if (!res.isEmpty())
                    if (!res.get().equals(ButtonType.CANCEL)) {
                        DAOMode = Mode.SQL;
                        factory = DAOFactory.getFactory(DAOMode);
                        connectionMode.setText("MySQL");
                        if (res.get().equals(ButtonType.YES))
                            mainTabPane.getTabs().clear();
                    }
            } else {
                DAOMode = Mode.SQL;
                factory = DAOFactory.getFactory(DAOMode);
                connectionMode.setText("MySQL");
            }
        }
    }

    public void toMemory() {
        if (DAOMode != Mode.MEMORY) {
            if (mainTabPane.getTabs().size() > 0) {
                var dialog = new Alert(AlertType.WARNING,
                        "Changer de mode rendra obsolète tous les onglets ouverts, les fermer ?", ButtonType.YES,
                        ButtonType.NO, ButtonType.CANCEL);
                var res = dialog.showAndWait();
                if (!res.isEmpty())
                    if (!res.get().equals(ButtonType.CANCEL)) {
                        DAOMode = Mode.MEMORY;
                        factory = DAOFactory.getFactory(DAOMode);
                        connectionMode.setText("Mémoire");
                        if (res.get().equals(ButtonType.YES))
                            mainTabPane.getTabs().clear();
                    }
            } else {
                DAOMode = Mode.MEMORY;
                factory = DAOFactory.getFactory(DAOMode);
                connectionMode.setText("Mémoire");
            }
        }
    }
}