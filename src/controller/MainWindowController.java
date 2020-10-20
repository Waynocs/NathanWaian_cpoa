package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.DAOFactory;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
    private static TabPane tabInstance;
    private static MainWindowController mainInstance;

    public static MainWindowController getInstance() {
        return mainInstance;
    }

    public static TabPane getMainTabPane() {
        return tabInstance;
    }

    public void close() {
        window.close();
    }

    public static DAOFactory factory;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        tabInstance = mainTabPane;
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

    public void addCategory() {

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

    public void addCustomer() {

    }

    public void addProduct() {

    }

    public static boolean removeProduct(Product prod) {
        if (factory.getOrderLineDAO().getAllFromProduct(prod.getId()).length != 0) {
            var alert = new Alert(AlertType.WARNING, "Une commande possède ce produit");
            alert.setTitle("Erreur suppression");
            alert.showAndWait();
            return false;
        }
        if (!factory.getProductDAO().delete(prod)) {
            var alert = new Alert(AlertType.ERROR, "Une erreur est survenue");
            alert.setTitle("Erreur suppression");
            alert.showAndWait();
            return false;
        } else
            return true;
    }

    public void addOrd() {
        addOrder();
    }

    public static void addOrder() {

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
        var tab = ProductsController.createControl();
        tabInstance.getTabs().add(tab);
        tabInstance.getSelectionModel().select(tab);
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
        var tab = ProductDetailController.createControl(prod);
        tabInstance.getTabs().add(tab);
        tabInstance.getSelectionModel().select(tab);
    }

    public static void editProduct(Product prod) {
        var tab = EditProductController.createControl(prod);
        tabInstance.getTabs().add(tab);
        tabInstance.getSelectionModel().select(tab);
    }

    public static void detailOrder(Order ord) {

    }
}