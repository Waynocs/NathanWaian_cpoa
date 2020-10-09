package application;

import java.net.URL;
import java.util.ResourceBundle;

import dao.DAOFactory;
import dao.DAOFactory.Mode;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import model.Category;

public class AjoutProduitController implements Initializable {

    @FXML
    private Label lblAffichage;
    @FXML
    private ChoiceBox<Category> cboxCateg;

    public void creerProduit() {
        this.lblAffichage.setText("Bonjour");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        var factory = DAOFactory.getFactory(Mode.MEMORY);
        cboxCateg.setItems(FXCollections.observableArrayList(factory.getCategoryDAO().getAll()));
    }

}
