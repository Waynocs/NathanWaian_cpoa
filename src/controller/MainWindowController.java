package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.DAOException;
import dao.DAOFactory;
import dao.DAOFactory.Mode;
import javafx.application.Platform;
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
    private static MenuButton locationInstance;
    private static TabPane tabInstance;
    private static ProgressBar loadingInstance;
    private static MainWindowController mainInstance;
    public static DAOFactory factory;
    private Mode DAOMode;
    private static ObservableList<MenuItem> locationMenu;

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
        tabInstance = mainTabPane;
        loadingInstance = loading;
        DAOMode = Mode.MEMORY;
        factory = DAOFactory.getFactory(DAOMode);
        locationInstance = location;
        connectionMode.setText("Mémoire");
        location.setText("Aucun onglet ouvert");
        locationMenu = location.getItems();
        mainTabPane.getTabs().addListener(new ListChangeListener<Tab>() {
            @Override
            public void onChanged(Change<? extends Tab> change) {
                while (change.next())
                    for (Tab tab : change.getRemoved())
                        locationMenu.remove((MenuItem) tab.getUserData());
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
        loadingInstance.setProgress(-1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                var tab = NewCategoryController.createControl();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                        tab.setOnSelectionChanged((e) -> {
                            if (tab.isSelected())
                                locationInstance.setText("Catégories>Nouvelle");
                            else if (tabInstance.getTabs().size() == 0)
                                locationInstance.setText("Aucun onglet ouvert");
                        });
                        locationInstance.setText("Catégories>Nouvelle");
                        var menuItem = new MenuItem("Catégories>Nouvelle");
                        tab.setUserData(menuItem);
                        menuItem.setOnAction((e) -> tabInstance.getSelectionModel().select(tab));
                        locationMenu.add(menuItem);
                        loadingInstance.setProgress(0);
                    }
                });
            }
        }).start();

    }

    public static void removeCategory(Category categ, Runnable deleted, Runnable notDeleted) {
        loadingInstance.setProgress(-1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    var prods = factory.getProductDAO().getAll();
                    for (Product product : prods) {
                        if (product.getCategory() == categ.getId()) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    var alert = new Alert(AlertType.WARNING, "Un produit utilise cette catégorie");
                                    alert.setTitle("Erreur suppression");
                                    alert.showAndWait();
                                    if (notDeleted != null)
                                        notDeleted.run();
                                    loadingInstance.setProgress(0);
                                }
                            });
                            return;
                        }
                    }
                    if (!factory.getCategoryDAO().delete(categ)) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                var alert = new Alert(AlertType.ERROR, "Une erreur est survenue");
                                alert.setTitle("Erreur suppression");
                                alert.showAndWait();
                                if (notDeleted != null)
                                    notDeleted.run();
                                loadingInstance.setProgress(0);
                            }
                        });
                    } else
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (deleted != null)
                                    deleted.run();
                                loadingInstance.setProgress(0);
                            }
                        });
                } catch (DAOException e) {
                    new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
                }
            }
        }).start();
    }

    public static void addCustomer() {

    }

    public void license() {
        try {
            URL fxmlURL = CategoriesController.class.getResource("../view/License.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            var tab = fxmlLoader.<TabPane>load().getTabs().get(0);
            tab.setOnSelectionChanged((e) -> {
                if (tab.isSelected())
                    location.setText("Aide>Licence");
                else if (tabInstance.getTabs().size() == 0)
                    locationInstance.setText("Aucun onglet ouvert");
            });
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
            location.setText("Aide>Licence");
            var menuItem = new MenuItem("Aide>Licence");
            tab.setUserData(menuItem);
            menuItem.setOnAction((e) -> tabInstance.getSelectionModel().select(tab));
            locationMenu.add(menuItem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void about() {

    }

    public static void addProduct() {
        loadingInstance.setProgress(-1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                var tab = NewProductController.createControl();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                        tab.setOnSelectionChanged((e) -> {
                            if (tab.isSelected())
                                locationInstance.setText("Produits>Nouveau");
                            else if (tabInstance.getTabs().size() == 0)
                                locationInstance.setText("Aucun onglet ouvert");
                        });
                        locationInstance.setText("Produits>Nouveau");
                        var menuItem = new MenuItem("Produits>Nouveau");
                        tab.setUserData(menuItem);
                        menuItem.setOnAction((e) -> tabInstance.getSelectionModel().select(tab));
                        locationMenu.add(menuItem);
                        loadingInstance.setProgress(0);
                    }
                });
            }
        }).start();

    }

    public void addCust() {
        addCustomer();
    }

    public void addProd() {
        addProduct();
    }

    public static void removeOrder(Order ord, Runnable deleted, Runnable notDeleted) {
        loadingInstance.setProgress(-1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!factory.getOrderDAO().delete(ord)) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                var alert = new Alert(AlertType.ERROR, "Une erreur est survenue");
                                alert.setTitle("Erreur suppression");
                                alert.showAndWait();
                                if (notDeleted != null)
                                    notDeleted.run();
                                loadingInstance.setProgress(0);
                            }
                        });
                    } else {
                        for (var line : factory.getOrderLineDAO().getAllFromOrder(ord.getId()))
                            factory.getOrderLineDAO().delete(line);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (deleted != null)
                                    deleted.run();
                                loadingInstance.setProgress(0);
                            }
                        });
                    }
                } catch (DAOException e) {
                    new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
                }
            }
        }).start();
    }

    public static void removeProduct(Product prod, Runnable deleted, Runnable notDeleted) {
        loadingInstance.setProgress(-1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (factory.getOrderLineDAO().getAllFromProduct(prod.getId()).length != 0) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                var alert = new Alert(AlertType.WARNING, "Une commande possède ce produit");
                                alert.setTitle("Erreur suppression");
                                alert.showAndWait();
                                if (notDeleted != null)
                                    notDeleted.run();
                                loadingInstance.setProgress(0);
                            }
                        });
                    } else if (!factory.getProductDAO().delete(prod)) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                var alert = new Alert(AlertType.ERROR, "Une erreur est survenue");
                                alert.setTitle("Erreur suppression");
                                alert.showAndWait();
                                if (notDeleted != null)
                                    notDeleted.run();
                                loadingInstance.setProgress(0);
                            }
                        });
                    } else
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (deleted != null)
                                    deleted.run();
                                loadingInstance.setProgress(0);
                            }
                        });
                } catch (DAOException e) {
                    new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
                }
            }
        }).start();
    }

    public void addOrd() {
        addOrder();
    }

    public static void addOrder() {

    }

    public static void runAsynchronously(Runnable fct) {
        loadingInstance.setProgress(-1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                fct.run();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loadingInstance.setProgress(0);
                    }
                });
            }
        }).start();
    }

    public void seeCategs() {
        seeCategories();
    }

    public static void seeCategories() {
        loadingInstance.setProgress(-1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                var tab = CategoriesController.createControl();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                        tab.setOnSelectionChanged((e) -> {
                            if (tab.isSelected())
                                locationInstance.setText("Catégories>Tout voir");
                            else if (tabInstance.getTabs().size() == 0)
                                locationInstance.setText("Aucun onglet ouvert");
                        });
                        locationInstance.setText("Catégories>Tout voir");
                        var menuItem = new MenuItem("Catégories>Tout voir");
                        tab.setUserData(menuItem);
                        menuItem.setOnAction((e) -> tabInstance.getSelectionModel().select(tab));
                        locationMenu.add(menuItem);
                        loadingInstance.setProgress(0);
                    }
                });
            }
        }).start();
    }

    public void seeCusts() {
        seeCustomers();
    }

    public static void seeCustomers() {

    }

    public void seeProds() {
        seeProducts();
    }

    public static void seeProducts() {
        loadingInstance.setProgress(-1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                var tab = ProductsController.createControl();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                        tab.setOnSelectionChanged((e) -> {
                            if (tab.isSelected())
                                locationInstance.setText("Produits>Tout voir");
                            else if (tabInstance.getTabs().size() == 0)
                                locationInstance.setText("Aucun onglet ouvert");
                        });
                        locationInstance.setText("Produits>Tout voir");
                        var menuItem = new MenuItem("Produits>Tout voir");
                        tab.setUserData(menuItem);
                        menuItem.setOnAction((e) -> tabInstance.getSelectionModel().select(tab));
                        locationMenu.add(menuItem);
                        loadingInstance.setProgress(0);
                    }
                });
            }
        }).start();
    }

    public void seeOrds() {
        seeOrders();
    }

    public static void seeOrders() {
        loadingInstance.setProgress(-1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                var tab = OrdersController.createControl();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                        tab.setOnSelectionChanged((e) -> {
                            if (tab.isSelected())
                                locationInstance.setText("Commandes>Tout voir");
                            else if (tabInstance.getTabs().size() == 0)
                                locationInstance.setText("Aucun onglet ouvert");
                        });
                        locationInstance.setText("Commandes>Tout voir");
                        var menuItem = new MenuItem("Commandes>Tout voir");
                        tab.setUserData(menuItem);
                        menuItem.setOnAction((e) -> tabInstance.getSelectionModel().select(tab));
                        locationMenu.add(menuItem);
                        loadingInstance.setProgress(0);
                    }
                });
            }
        }).start();
    }

    public static void detailCategory(Category categ) {
        loadingInstance.setProgress(-1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                var tab = CategoryDetailController.createControl(categ);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                        tab.setOnSelectionChanged((e) -> {
                            if (tab.isSelected())
                                locationInstance.setText("Catégories>Détail");
                            else if (tabInstance.getTabs().size() == 0)
                                locationInstance.setText("Aucun onglet ouvert");
                        });
                        locationInstance.setText("Catégories>Détail");
                        var menuItem = new MenuItem("Catégories>Détail");
                        tab.setUserData(menuItem);
                        menuItem.setOnAction((e) -> tabInstance.getSelectionModel().select(tab));
                        locationMenu.add(menuItem);
                        loadingInstance.setProgress(0);
                    }
                });
            }
        }).start();
    }

    public static void detailCustomer(Customer cust) {

    }

    public static void detailProduct(Product prod) {
        loadingInstance.setProgress(-1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                var tab = ProductDetailController.createControl(prod);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                        tab.setOnSelectionChanged((e) -> {
                            if (tab.isSelected())
                                locationInstance.setText("Produits>Détail");
                            else if (tabInstance.getTabs().size() == 0)
                                locationInstance.setText("Aucun onglet ouvert");
                        });
                        locationInstance.setText("Produits>Détail");
                        var menuItem = new MenuItem("Produits>Détail");
                        tab.setUserData(menuItem);
                        menuItem.setOnAction((e) -> tabInstance.getSelectionModel().select(tab));
                        locationMenu.add(menuItem);
                        loadingInstance.setProgress(0);
                    }
                });
            }
        }).start();
    }

    public static void editProduct(Product prod, ProductDetailController controller) {
        loadingInstance.setProgress(-1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                var editor = EditProductController.createController(prod, controller);
                editor.tab.setOnClosed((Event) -> {
                    tabInstance.getTabs().remove(editor.tab);
                    if (editor.reopenDetails) {
                        tabInstance.getTabs().add(controller.tab);
                        tabInstance.getSelectionModel().select(controller.tab);
                        editor.tab.setOnSelectionChanged((e) -> {
                            if (editor.tab.isSelected())
                                locationInstance.setText("Produits>Éditer");
                            else if (tabInstance.getTabs().size() == 0)
                                locationInstance.setText("Aucun onglet ouvert");
                        });
                        locationInstance.setText("Produits>Éditer");
                        var menuItem = new MenuItem("Produits>Éditer");
                        editor.tab.setUserData(menuItem);
                        menuItem.setOnAction((e) -> tabInstance.getSelectionModel().select(editor.tab));
                        locationMenu.add(menuItem);
                        if (editor.saved)
                            controller.refresh();
                    }
                });
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().remove(controller.tab);
                        tabInstance.getTabs().add(editor.tab);
                        tabInstance.getSelectionModel().select(editor.tab);
                        loadingInstance.setProgress(0);
                    }
                });
            }
        }).start();
    }

    public static void editCategory(Category categ, CategoryDetailController controller) {
        loadingInstance.setProgress(-1);
        new Thread(new Runnable() {

            @Override
            public void run() {
                var editor = EditCategoryController.createController(categ, controller);
                editor.tab.setOnClosed((Event) -> {
                    tabInstance.getTabs().remove(editor.tab);
                    if (editor.reopenDetails) {
                        tabInstance.getTabs().add(controller.tab);
                        tabInstance.getSelectionModel().select(controller.tab);
                        editor.tab.setOnSelectionChanged((e) -> {
                            if (editor.tab.isSelected())
                                locationInstance.setText("Catégories>Éditer");
                            else if (tabInstance.getTabs().size() == 0)
                                locationInstance.setText("Aucun onglet ouvert");
                        });
                        locationInstance.setText("Catégories>Éditer");
                        var menuItem = new MenuItem("Catégories>Éditer");
                        editor.tab.setUserData(menuItem);
                        menuItem.setOnAction((e) -> tabInstance.getSelectionModel().select(editor.tab));
                        locationMenu.add(menuItem);
                        if (editor.saved)
                            controller.refresh();
                    }
                });
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        tabInstance.getTabs().remove(controller.tab);
                        tabInstance.getTabs().add(editor.tab);
                        tabInstance.getSelectionModel().select(editor.tab);
                        loadingInstance.setProgress(0);
                    }
                });
            }
        }).start();
    }

    public static void editOrder(Order ord, OrderDetailController controller) {
        loadingInstance.setProgress(-1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                var editor = EditOrderController.createController(ord, controller);
                editor.tab.setOnClosed((e1) -> Platform.runLater(() -> {
                    tabInstance.getTabs().remove(editor.tab);
                    if (editor.reopenDetails) {
                        tabInstance.getTabs().add(controller.tab);
                        tabInstance.getSelectionModel().select(controller.tab);
                        editor.tab.setOnSelectionChanged((e2) -> {
                            if (editor.tab.isSelected())
                                locationInstance.setText("Commandes>Éditer");
                            else if (tabInstance.getTabs().size() == 0)
                                locationInstance.setText("Aucun onglet ouvert");
                        });
                        locationInstance.setText("Commandes>Éditer");
                        var menuItem = new MenuItem("Commandes>Éditer");
                        editor.tab.setUserData(menuItem);
                        menuItem.setOnAction((e) -> tabInstance.getSelectionModel().select(editor.tab));
                        locationMenu.add(menuItem);
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
                        loadingInstance.setProgress(0);
                    }
                });
            }
        }).start();
    }

    // TODO take count of the running op
    public static void detailOrder(Order ord) {
        loadingInstance.setProgress(-1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                var tab = OrderDetailController.createControl(ord);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                        tab.setOnSelectionChanged((e) -> {
                            if (tab.isSelected())
                                locationInstance.setText("Commandes>Détail");
                            else if (tabInstance.getTabs().size() == 0)
                                locationInstance.setText("Aucun onglet ouvert");
                        });
                        locationInstance.setText("Commandes>Détail");
                        var menuItem = new MenuItem("Commandes>Détail");
                        tab.setUserData(menuItem);
                        menuItem.setOnAction((e) -> tabInstance.getSelectionModel().select(tab));
                        locationMenu.add(menuItem);
                        loadingInstance.setProgress(0);
                    }
                });
            }
        }).start();
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