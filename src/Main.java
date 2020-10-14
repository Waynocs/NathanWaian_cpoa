import java.net.URL;

import application.AjoutProduitController;
import dao.DAOFactory;
import dao.DAOFactory.Mode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainMenu;
import view.Utilities;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            {
                URL fxmlURL = getClass().getResource("./testt.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
                Node root = fxmlLoader.load();
                Scene scene = new Scene((AnchorPane) root, 600, 400);
                /*
                 * scene.getStylesheets().add(getClass().getResource("application.cs
                 * s").toExternalForm());
                 */
                primaryStage.setScene(scene);
                primaryStage.setTitle("Ma première fenêtre JavaFX");
                primaryStage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void launchUI(String[] args) {
        switch (Utilities.getUserSelection("Choose a mode :\n1. MySQL\n2. Saved in memory", 2)) {
            case 1:
                AjoutProduitController.Factory = DAOFactory.getFactory(Mode.SQL);
                break;
            case 2:
                AjoutProduitController.Factory = DAOFactory.getFactory(Mode.MEMORY);
                break;
        }
        launch(args);
    }

    public static void main(String[] args) {
        if (Utilities.getUserSelection("Use the UI ?\n1. Yes\n2. No", 2) == 2)
            MainMenu.start();
        else
            launchUI(args);
    }
}
