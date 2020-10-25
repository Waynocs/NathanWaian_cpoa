package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.DAOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import model.Customer;

public class EditCustomerController implements Initializable {

    @FXML
    public TextField surname;
    @FXML
    public TextField name;
    @FXML
    public TextField identity;
    @FXML
    public TextField password;
    @FXML
    public TextField number;
    @FXML
    public TextField street;
    @FXML
    public TextField postalcode;
    @FXML
    public TextField city;
    @FXML
    public TextField country;
    public Tab tab;
    public Customer customer;
    public CustomerDetailController detailController;
    public boolean reopenDetails;
    public boolean saved;

    public static EditCustomerController createController(Customer custo, CustomerDetailController detail) {
        try {
            URL fxmlURL = EditCustomerController.class.getResource("../view/EditCustomer.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            fxmlLoader.<TabPane>load();
            var controller = fxmlLoader.<EditCustomerController>getController();
            controller.setupFields(custo);
            controller.detailController = detail;
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    public void setupFields(Customer custo) {
        customer = custo;
        surname.setText(customer.getSurname());
        name.setText(customer.getName());
        identity.setText(customer.getIdentifier());
        password.setText(customer.getPwd());
        number.setText(customer.getAddressNumber());
        street.setText(customer.getAddressStreet());
        postalcode.setText(customer.getAddressPostalCode());
        city.setText(customer.getAddressCity());
        country.setText(customer.getAddressCountry());

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        reopenDetails = false;

    }

    public void reset() {

        surname.setText(customer.getSurname());
        name.setText(customer.getName());
        identity.setText(customer.getIdentifier());
        password.setText(customer.getPwd());
        number.setText(customer.getAddressNumber());
        street.setText(customer.getAddressStreet());
        postalcode.setText(customer.getAddressPostalCode());
        city.setText(customer.getAddressCity());
        country.setText(customer.getAddressCountry());
    }

    public void cancel() {
        saved = false;
        reopenDetails = true;
        tab.getOnClosed().handle(null);
    }

    public void save() {
        MainWindowController.runAsynchronously(() -> {
            if (surname.getText().length() == 0)
                return new AlertPair("Renseignez un nom", AlertType.WARNING);
            else if (name.getText().length() == 0)
                return new AlertPair("Renseignez un prénom", AlertType.WARNING);
            else if (identity.getText().length() == 0)
                return new AlertPair("Renseignez un identifiant", AlertType.WARNING);
            else if (password.getText().length() == 0)
                return new AlertPair("Renseignez un mot de passe", AlertType.WARNING);
            else if (number.getText().length() == 0)
                return new AlertPair("Renseignez un numéro d'adresse", AlertType.WARNING);
            else if (street.getText().length() == 0)
                return new AlertPair("Renseignez une voie d'adresse", AlertType.WARNING);
            else if (postalcode.getText().length() == 0)
                return new AlertPair("Renseignez un code postal", AlertType.WARNING);
            else if (city.getText().length() == 0)
                return new AlertPair("Renseignez une ville", AlertType.WARNING);
            else if (country.getText().length() == 0)
                return new AlertPair("Renseignez un pays", AlertType.WARNING);

            customer.setSurname(surname.getText());
            customer.setName(name.getText());
            customer.setIdentifier(identity.getText());
            customer.setPwd(password.getText());
            customer.setAddressNumber(number.getText());
            customer.setAddressStreet(street.getText());
            customer.setAddressPostalCode(postalcode.getText());
            customer.setAddressCity(city.getText());
            customer.setAddressCountry(country.getText());
            try {
                if (MainWindowController.factory.getCustomerDAO().update(customer)) {
                    saved = true;
                    reopenDetails = true;
                    return new AlertPair();
                } else
                    return new AlertPair("Impossible de modifier ce client", AlertType.ERROR);

            } catch (DAOException e) {
                return new AlertPair(e.getMessage(), AlertType.ERROR);
            }
        }, (pair) -> {
            if (pair.getKey() != null)
                new Alert(pair.getValue(), pair.getKey()).showAndWait();
            else
                tab.getOnClosed().handle(null);
        });
    }
}
