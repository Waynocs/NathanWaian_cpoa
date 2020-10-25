package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.DAOException;
import javafx.application.Platform;
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
            MainWindowController.runAsynchronously(() -> {
                if (surname.getText().length() == 0) {
                    Platform.runLater(() -> new Alert(AlertType.WARNING, "Renseignez un nom").showAndWait());
                    return;
                } else if (name.getText().length() == 0) {
                    Platform.runLater(() -> new Alert(AlertType.WARNING, "Renseignez un Prénom").showAndWait());
                    return;
                } else if (identity.getText().length() == 0) {
                    Platform.runLater(() -> new Alert(AlertType.WARNING, "Renseignez un identifiant").showAndWait());
                    return;
                } else if (password.getText().length() == 0) {
                    Platform.runLater(() -> new Alert(AlertType.WARNING, "Renseignez un Mot de passe").showAndWait());
                    return;
                } else if (number.getText().length() == 0) {
                    Platform.runLater(
                            () -> new Alert(AlertType.WARNING, "Renseignez un numéro d'adresse").showAndWait());
                    return;
                } else if (street.getText().length() == 0) {
                    Platform.runLater(
                            () -> new Alert(AlertType.WARNING, "Renseignez une voie d'adresse").showAndWait());
                    return;
                } else if (postalcode.getText().length() == 0) {
                    Platform.runLater(() -> new Alert(AlertType.WARNING, "Renseignez un code postal").showAndWait());
                    return;
                } else if (city.getText().length() == 0) {
                    Platform.runLater(() -> new Alert(AlertType.WARNING, "Renseignez une ville").showAndWait());
                    return;
                } else if (country.getText().length() == 0) {
                    Platform.runLater(() -> new Alert(AlertType.WARNING, "Renseignez un pays").showAndWait());
                    return;
                }
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
                        tab.getOnClosed().handle(null);
                    } else
                        Platform.runLater(
                                () -> new Alert(AlertType.ERROR, "Impossible de modifier ce client").showAndWait());

                } catch (DAOException e) {
                    Platform.runLater(() -> new Alert(AlertType.ERROR, e.getMessage()).showAndWait());
                }
            });
        });
    }
}
