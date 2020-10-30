package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
        runAsynchronously(() -> {
            var tab = NewCategoryController.createControl();
            tab.setUserData("Catégories>Nouvelle");
            return tab;
        }, (tab) -> {
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
        });
    }

    public static void removeCategory(Category categ, Runnable deleted, Runnable notDeleted) {
        runAsynchronously(() -> {
            try {
                var prods = factory.getProductDAO().getAll();
                for (Product product : prods) {
                    if (product.getCategory() == categ.getId())
                        return new AlertPair("Un produit utilise cette catégorie", AlertType.WARNING);
                }
                if (!factory.getCategoryDAO().delete(categ))
                    return new AlertPair("Une erreur est survenue", AlertType.ERROR);
                else
                    return new AlertPair();
            } catch (DAOException e) {
                return new AlertPair(e.getMessage(), AlertType.ERROR);
            }
        }, (pair) -> {
            if (pair.getKey() != null) {
                new Alert(pair.getValue(), pair.getKey()).showAndWait();
                if (notDeleted != null)
                    notDeleted.run();
            } else if (deleted != null)
                deleted.run();
        });

    }

    public static void addCustomer() {
        runAsynchronously(() -> {
            var tab = NewCustomerController.createControl();
            tab.setUserData("Clients>Nouveau");
            return tab;
        }, (tab) -> {
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
        });

    }

    public static void removeCustomer(Customer custo, Runnable deleted, Runnable notDeleted) {
        runAsynchronously(() -> {
            try {
                var ords = factory.getOrderDAO().getAll();
                for (Order ord : ords)
                    if (ord.getCustomer() == custo.getId())
                        return new AlertPair("Une commande est associée à ce client", AlertType.WARNING);
                if (!factory.getCustomerDAO().delete(custo))
                    return new AlertPair("Une erreur est survenue", AlertType.ERROR);
                else
                    return new AlertPair();
            } catch (DAOException e) {
                return new AlertPair(e.getMessage(), AlertType.ERROR);
            }
        }, (pair) -> {
            if (pair.getKey() != null) {
                new Alert(pair.getValue(), pair.getKey()).showAndWait();
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
        try {
            URL fxmlURL = CategoriesController.class.getResource("../view/About.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            var tab = fxmlLoader.<TabPane>load().getTabs().get(0);
            tab.setUserData("Aide>A propos");
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void addProduct() {
        runAsynchronously(() -> {
            var tab = NewProductController.createControl();
            tab.setUserData("Produits>Nouveau");
            return tab;
        }, (tab) -> {

            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
        });
    }

    public void addCust() {
        addCustomer();
    }

    public void addProd() {
        addProduct();
    }

    public static void removeOrder(Order ord, Runnable deleted, Runnable notDeleted) {
        runAsynchronously(() -> {
            try {
                if (!factory.getOrderDAO().delete(ord))
                    return new AlertPair("Une erreur est survenue", AlertType.ERROR);
                else
                    return new AlertPair();
            } catch (

            DAOException e) {
                return new AlertPair(e.getMessage(), AlertType.ERROR);
            }
        }, (pair) -> {
            if (pair.getKey() != null) {
                new Alert(pair.getValue(), pair.getKey()).showAndWait();
                if (notDeleted != null)
                    notDeleted.run();
            } else if (deleted != null)
                deleted.run();
        });
    }

    public static void removeProduct(Product prod, Runnable deleted, Runnable notDeleted) {
        runAsynchronously(() -> {
            try {
                if (factory.getOrderLineDAO().getAllFromProduct(prod.getId()).length != 0)
                    return new AlertPair("Une commande possède ce produit", AlertType.WARNING);
                else if (!factory.getProductDAO().delete(prod))
                    return new AlertPair("Une erreur est survenue", AlertType.ERROR);
                else
                    return new AlertPair();
            } catch (DAOException e) {
                return new AlertPair(e.getMessage(), AlertType.ERROR);
            }
        }, (pair) -> {
            if (pair.getKey() != null) {
                new Alert(pair.getValue(), pair.getKey()).showAndWait();
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
        runAsynchronously(() -> {
            var tab = NewOrderController.createControl();
            tab.setUserData("Commandes>Nouvelle");
            return tab;
        }, (tab) -> {
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
        });
    }

    public static void runAsynchronously(Runnable subthread) {
        runAsynchronously(subthread, null);
    }

    public static <T> void runAsynchronously(Supplier<? extends T> subthread, Consumer<T> mainthread) {
        var ptr = new Ptr<T>();
        runAsynchronously(() -> {
            ptr.value = subthread.get();
        }, () -> mainthread.accept(ptr.value));
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
        runAsynchronously(() -> {
            var tab = CategoriesController.createControl();
            tab.setUserData("Catégories>Tout voir");
            return tab;
        }, (tab) -> {
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
        });
    }

    public void seeCusts() {
        seeCustomers();
    }

    public static void seeCustomers() {
        runAsynchronously(() -> {
            var tab = CustomersController.createControl();
            tab.setUserData("Clients>Tout voir");
            return tab;
        }, (tab) -> {
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
        });

    }

    public void seeProds() {
        seeProducts();
    }

    public static void seeProducts() {
        runAsynchronously(() -> {
            var tab = ProductsController.createControl();
            tab.setUserData("Produits>Tout voir");
            return tab;
        }, (tab) -> {
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
        });
    }

    public void seeOrds() {
        seeOrders();
    }

    public static void seeOrders() {
        runAsynchronously(() -> {
            var tab = OrdersController.createControl();
            tab.setUserData("Commandes>Tout voir");
            return tab;
        }, (tab) -> {
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
        });

    }

    public static void seeOrders(Customer cust) {
        runAsynchronously(() -> {
            var tab = OrdersController.createControl(cust);
            tab.setUserData("Commandes>Tout voir");
            return tab;
        }, (tab) -> {
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
        });

    }

    public static void detailCategory(Category categ) {
        runAsynchronously(() -> {
            var tab = CategoryDetailController.createControl(categ);
            tab.setUserData("Catégories>Détail");
            return tab;
        }, (tab) -> {
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
        });
    }

    public static void detailCustomer(Customer custo) {
        runAsynchronously(() -> {
            var tab = CustomerDetailController.createControl(custo);
            tab.setUserData("Clients>Détail");
            return tab;
        }, (tab) -> {
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
        });

    }

    public static void detailProduct(Product prod) {
        runAsynchronously(() -> {
            var tab = ProductDetailController.createControl(prod);
            tab.setUserData("Produits>Détail");
            return tab;
        }, (tab) -> {
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
        });
    }

    public static void editProduct(Product prod, ProductDetailController controller) {
        runAsynchronously(() -> {
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
            return editor;
        }, (editor) -> {
            tabInstance.getTabs().remove(controller.tab);
            tabInstance.getTabs().add(editor.tab);
            tabInstance.getSelectionModel().select(editor.tab);
        });

    }

    public static void editCategory(Category categ, CategoryDetailController controller) {
        runAsynchronously(() -> {
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
            return editor;
        }, (editor) -> {
            tabInstance.getTabs().remove(controller.tab);
            tabInstance.getTabs().add(editor.tab);
            tabInstance.getSelectionModel().select(editor.tab);
        });
    }

    public static void editOrder(Order ord, OrderDetailController controller) {
        runAsynchronously(() -> {
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
            return editor;
        }, (editor) -> {
            tabInstance.getTabs().remove(controller.tab);
            tabInstance.getTabs().add(editor.tab);
            tabInstance.getSelectionModel().select(editor.tab);
        });
    }

    public static void editCustomer(Customer custo, CustomerDetailController controller) {
        runAsynchronously(() -> {
            var editor = EditCustomerController.createController(custo, controller);
            editor.tab.setUserData("Clients>Éditer");
            editor.tab.setOnClosed((e1) -> Platform.runLater(() -> {
                tabInstance.getTabs().remove(editor.tab);
                if (editor.reopenDetails) {
                    tabInstance.getTabs().add(controller.tab);
                    tabInstance.getSelectionModel().select(controller.tab);
                    if (editor.saved)
                        controller.refresh();
                }
            }));
            return editor;
        }, (editor) -> {
            tabInstance.getTabs().remove(controller.tab);
            tabInstance.getTabs().add(editor.tab);
            tabInstance.getSelectionModel().select(editor.tab);
        });
    }

    public static void detailOrder(Order ord) {
        runAsynchronously(() -> {
            var tab = OrderDetailController.createControl(ord);
            tab.setUserData("Commandes>Détail");
            return tab;
        }, (tab) -> {
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
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