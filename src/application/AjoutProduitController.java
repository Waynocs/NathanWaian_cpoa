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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Category;

public class AjoutProduitController implements Initializable {

    @FXML
    private Label lblAffichage;
    @FXML
    private Label lblNom;
    @FXML
    private Label lblDesc;
    @FXML
    private Label lblTarif;
    @FXML
    private ChoiceBox<Category> cboxCateg;
    @FXML
    private TextField editNom;
    @FXML
    private TextArea editDesc;
    @FXML
    private TextField editTarif;

    public void creerProduit() {
        String name = editNom.getText().trim();
        String desc = editDesc.getText().trim();
        float cost = 0;
        try {
            cost = Float.parseFloat(editTarif.getText().trim());
        } catch (NumberFormatException e) {

        }

        if (name.isEmpty()) {
            String errorName = "Nom non saisie ou non valide";
            this.lblNom.setText(errorName);
        }
        if (desc.isEmpty()) {
            String errorDesc = "Description non saisie";
            this.lblDesc.setText(errorDesc);
        }
        if (cost <= 0) {
            String errorCost = "Tarif non saisie ou non valide";
            this.lblTarif.setText(errorCost);
        }

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        var factory = DAOFactory.getFactory(Mode.MEMORY);
        cboxCateg.setItems(FXCollections.observableArrayList(factory.getCategoryDAO().getAll()));

    }

    public String toString() {
        return this.editNom + " " + this.editDesc + this.editTarif;
    }

}
