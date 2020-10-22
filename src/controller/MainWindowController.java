package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.DAOFactory;
import dao.DAOFactory.Mode;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
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
    private static TabPane tabInstance;
    private static ProgressBar loadingInstance;
    private static MainWindowController mainInstance;
    public static DAOFactory factory;
    private Mode DAOMode;

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
        connectionMode.setText("Mémoire");
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

    }

    public static boolean removeCategory(Category categ) {
        if (!factory.getCategoryDAO().delete(categ)) {
            var alert = new Alert(AlertType.ERROR, "Une erreur est survenue");
            alert.setTitle("Erreur suppression");
            alert.showAndWait();
            return false;
        } else
            return true;
    }

    public static void addCustomer() {

    }

    public void license() {
        try {
            URL fxmlURL = CategoriesController.class.getResource("../view/License.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            var tab = fxmlLoader.<TabPane>load().getTabs().get(0);
            tabInstance.getTabs().add(tab);
            tabInstance.getSelectionModel().select(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void about() {

    }

    public static void addProduct() {
        loadingInstance.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                var tab = NewProductController.createControl();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                        loadingInstance.setVisible(false);
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

    public static void removeProduct(Product prod, Runnable deleted, Runnable notDeleted) {
        loadingInstance.setVisible(true);
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
                                loadingInstance.setVisible(false);
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
                                loadingInstance.setVisible(false);
                            }
                        });
                    } else
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (deleted != null)
                                    deleted.run();
                                loadingInstance.setVisible(false);
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
        loadingInstance.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                fct.run();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loadingInstance.setVisible(false);
                    }
                });
            }
        }).start();
    }

    public void seeCategs() {
        seeCategories();
    }

    public static void seeCategories() {
        var tab = CategoriesController.createControl();
        tabInstance.getTabs().add(tab);
        tabInstance.getSelectionModel().select(tab);
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
        loadingInstance.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                var tab = ProductsController.createControl();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                        loadingInstance.setVisible(false);
                    }
                });
            }
        }).start();
    }

    public void seeOrds() {
        seeOrders();
    }

    public static void seeOrders() {

    }

    public static void detailCategory(Category categ) {
    }

    public static void detailCustomer(Customer cust) {

    }

    public static void detailProduct(Product prod) {
        loadingInstance.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                var tab = ProductDetailController.createControl(prod);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabInstance.getTabs().add(tab);
                        tabInstance.getSelectionModel().select(tab);
                        loadingInstance.setVisible(false);
                    }
                });
            }
        }).start();
    }

    public static void editProduct(Product prod, ProductDetailController controller) {
        loadingInstance.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                var editor = EditProductController.createController(prod, controller);
                editor.tab.setOnClosed((Event) -> {
                    tabInstance.getTabs().remove(editor.tab);
                    if (editor.reopenDetails) {
                        tabInstance.getTabs().add(controller.tab);
                        tabInstance.getSelectionModel().select(controller.tab);
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
                        loadingInstance.setVisible(false);
                    }
                });
            }
        }).start();
    }

    public static void detailOrder(Order ord) {

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