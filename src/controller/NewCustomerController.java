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
import javafx.scene.control.PasswordField;
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
    public PasswordField password;
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

            var customer = new Customer(0, name.getText(), surname.getText(), identity.getText(), password.getText(),
                    street.getText(), number.getText(), postalcode.getText(), city.getText(), country.getText());

            try {
                if (MainWindowController.factory.getCustomerDAO().create(customer) != null)
                    return new AlertPair();
                else
                    return new AlertPair("Impossible de créer ce client", AlertType.ERROR);
            } catch (DAOException e) {
                return new AlertPair(e.getMessage(), AlertType.ERROR);
            }
        }, (pair) -> {
            if (pair.getKey() != null)
                new Alert(pair.getValue(), pair.getKey()).showAndWait();
            else
                MainWindowController.getMainTabPane().getTabs().remove(tab);
        });
    }

}
