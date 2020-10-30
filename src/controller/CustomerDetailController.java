package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import model.Customer;

public class CustomerDetailController implements Initializable {

    @FXML
    public Label id;
    @FXML
    public Label surname;
    @FXML
    public Label name;
    @FXML
    public Label identity;
    @FXML
    public Label password;
    @FXML
    public Label number;
    @FXML
    public Label street;
    @FXML
    public Label postalcode;
    @FXML
    public Label city;
    @FXML
    public Label country;
    @FXML
    public CheckBox seePass;

    public Tab tab;
    public Customer customer;

    public static Tab createControl(Customer custo) {
        try {
            URL fxmlURL = CustomerDetailController.class.getResource("../view/CustomerDetail.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            var control = fxmlLoader.<TabPane>load();
            var controller = fxmlLoader.<CustomerDetailController>getController();
            controller.setupFields(custo);
            return control.getTabs().get(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    public void setupFields(Customer custo) {
        customer = custo;
        tab.setText("Détail:" + customer.getName() + " " + customer.getSurname());

        id.setText("ID : " + customer.getId());
        surname.setText("Nom : " + customer.getSurname());
        name.setText("Prénom : " + customer.getName());
        identity.setText("Identifiant : " + customer.getIdentifier());
        password.setText("Mot de passe : " + Utilities.getHiddenString(customer.getPwd()));
        number.setText("Numéro d'adresse : " + customer.getAddressNumber());
        street.setText("Voie d'adresse : " + customer.getAddressStreet());
        postalcode.setText("Code postal : " + customer.getAddressPostalCode());
        city.setText("Ville : " + customer.getAddressCity());
        country.setText("Pays : " + customer.getAddressCountry());

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        seePass.setOnAction((e) -> password.setText("Mot de passe : "
                + (seePass.isSelected() ? customer.getPwd() : Utilities.getHiddenString(customer.getPwd()))));
    }

    public void refresh() {
        MainWindowController.runAsynchronously(
                () -> customer = MainWindowController.factory.getCustomerDAO().getById(customer.getId()), () -> {
                    tab.setText("Détail:" + customer.getName());
                    id.setText("ID : " + customer.getId());
                    surname.setText("Nom : " + customer.getSurname());
                    name.setText("Prénom : " + customer.getName());
                    identity.setText("Identifiant : " + customer.getIdentifier());
                    password.setText("Mot de passe : " + (seePass.isSelected() ? customer.getPwd()
                            : Utilities.getHiddenString(customer.getPwd())));
                    number.setText("Numéro d'adresse : " + customer.getAddressNumber());
                    street.setText("Voie d'adresse : " + customer.getAddressStreet());
                    postalcode.setText("Code postal : " + customer.getAddressPostalCode());
                    city.setText("Ville : " + customer.getAddressCity());
                    country.setText("Pays : " + customer.getAddressCountry());
                });

    }

    public void edit() {
        MainWindowController.editCustomer(customer, this);
    }

    public void remove() {
        MainWindowController.removeCustomer(customer, () -> {
            EventHandler<Event> handler = tab.getOnClosed();
            if (null != handler) {
                handler.handle(null);
            } else {
                tab.getTabPane().getTabs().remove(tab);
            }
        }, null);
    }

    public void seeOrders() {
        MainWindowController.seeOrders(customer);
    }
}
