package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AjoutProduitController {

    @FXML
    private Label lblAffichage;

    public void creerProduit() {
        this.lblAffichage.setText("Bonjour");
    }

}
