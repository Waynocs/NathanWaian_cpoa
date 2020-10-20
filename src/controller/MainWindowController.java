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
                    }
                    ke.consume();
                }
            }
        });
    }

    public void addCategory() {

    }

    public void addCustomer() {

    }

    public void addProduct() {

    }

    public static boolean removeProduct(Product prod) {
        if (!factory.getProductDAO().delete(prod)) {
            var alert = new Alert(AlertType.ERROR, "Un erreur est survenue");
            alert.setTitle("Erreur suppression");
            alert.showAndWait();
            return false;
        } else
            return true;
    }

    public void addOrder() {

    }

    public void seeCategories() {
        var tab = CategoriesController.createControl();
        mainTabPane.getTabs().add(tab);
        mainTabPane.getSelectionModel().select(tab);
    }

    public void seeCustomers() {

    }

    public void seeProducts() {
        var tab = ProductsController.createControl();
        mainTabPane.getTabs().add(tab);
        mainTabPane.getSelectionModel().select(tab);
    }

    public void seeOrders() {

    }

    public void detailCategory(Integer id) {

    }

    public void detailCustomer(Integer id) {

    }

    public void detailProduct(Integer id) {

    }

    public void detailOrder(Integer id) {

    }
}
