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
import javafx.scene.paint.Color;
import model.Category;
import model.Product;

public class AjoutProduitController implements Initializable {
    public static DAOFactory Factory;
    @FXML
    private Label lblAffichage;
    @FXML
    private ChoiceBox<Category> cboxCateg;
    @FXML
    private TextField editNom;
    @FXML
    private TextArea editDesc;
    @FXML
    private TextField editTarif;

    public void creerProduit() {

        boolean verif = true;
        String name = editNom.getText().trim();
        String desc = editDesc.getText().trim();
        float cost = 0;
        try {
            cost = Float.parseFloat(editTarif.getText().trim());
        } catch (NumberFormatException e) {
            verif = false;
        }

        if (name.isEmpty()) {
            lblAffichage.setText("Nom non saisi ou non valide");
            lblAffichage.setTextFill(Color.web("#FF0000"));
            verif = false;
        } else if (desc.isEmpty()) {
            lblAffichage.setText("Description non saisie");
            lblAffichage.setTextFill(Color.web("#FF0000"));
            verif = false;
        } else if (cost <= 0) {
            lblAffichage.setText("Tarif non saisi ou non valide");
            lblAffichage.setTextFill(Color.web("#FF0000"));
            verif = false;
        } else if (cboxCateg.getSelectionModel().getSelectedIndex() == -1) {
            lblAffichage.setText("Catégorie non saisie");
            lblAffichage.setTextFill(Color.web("#FF0000"));
            verif = false;
        } else
            lblAffichage.setText("");
        if (verif == true) {

            Product prod = new Product(1, name, cost, desc, cboxCateg.getSelectionModel().getSelectedItem().getId(),
                    "null");
            if (Factory.getProductDAO().create(prod) != null) {
                lblAffichage.setText("Produit créé !");
                lblAffichage.setTextFill(Color.web("#000000"));
            } else {
                lblAffichage.setText("Erreur lors de la création");
                lblAffichage.setTextFill(Color.web("#FF0000"));
            }

        }

    }

    public void initializeLabel() {
        lblAffichage.setText("");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cboxCateg.setItems(FXCollections.observableArrayList(Factory.getCategoryDAO().getAll()));
        initializeLabel();

    }

    @Override
    public String toString() {
        return this.editNom.getText() + " ( " + this.cboxCateg.getSelectionModel().getSelectedItem().getName() + " ) "
                + this.editTarif.getText();

    }

}
