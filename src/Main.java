import java.net.URL;

import controller.MainWindowController;
import dao.DAOFactory;
import dao.DAOFactory.Mode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.MainMenu;
import view.Utilities;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            {
                MainWindowController.window = primaryStage;
                URL fxmlURL = getClass().getResource("./view/MainWindow.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
                Node root = fxmlLoader.load();
                Scene scene = new Scene((BorderPane) root);
                /*
                 * scene.getStylesheets().add(getClass().getResource("application.cs
                 * s").toExternalForm());
                 */
                primaryStage.setScene(scene);
                primaryStage.setTitle("Business Pro Euro Simulator Deluxe Edition");
                primaryStage.setWidth(1000);
                primaryStage.setHeight(700);
                primaryStage.getIcons().add(new Image("assets/icons/icon.png"));
                primaryStage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void launchUI(String[] args) {
        switch (Utilities.getUserSelection("Choose a mode :\n1. MySQL\n2. Saved in memory", 2)) {
            case 1:
                MainWindowController.factory = DAOFactory.getFactory(Mode.SQL);
                break;
            case 2:
                MainWindowController.factory = DAOFactory.getFactory(Mode.MEMORY);
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
