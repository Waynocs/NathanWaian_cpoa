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

public class NewCustomerController implements Initializable {

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
    @FXML
    public Tab tab;

    public static Tab createControl() {
        try {
            URL fxmlURL = CustomersController.class.getResource("../view/NewCustomer.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            return fxmlLoader.<TabPane>load().getTabs().get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    public void cancel() {
        MainWindowController.getMainTabPane().getTabs().remove(tab);
    }

    public void save() {
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
                Platform.runLater(() -> new Alert(AlertType.WARNING, "Renseignez un numéro d'adresse").showAndWait());
                return;
            } else if (street.getText().length() == 0) {
                Platform.runLater(() -> new Alert(AlertType.WARNING, "Renseignez une voie d'adresse").showAndWait());
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

            var customer = new Customer(0, surname.getText(), name.getText(), identity.getText(), password.getText(),
                    number.getText(), street.getText(), postalcode.getText(), city.getText(), country.getText());

            try {
                if (MainWindowController.factory.getCustomerDAO().create(customer) != null)
                    Platform.runLater(() -> MainWindowController.getMainTabPane().getTabs().remove(tab));
                else
                    Platform.runLater(() -> new Alert(AlertType.ERROR, "Impossible de créer ce client").showAndWait());
            } catch (DAOException e) {
                Platform.runLater(() -> new Alert(AlertType.ERROR, e.getMessage()).showAndWait());
            }
        });
    }

}
