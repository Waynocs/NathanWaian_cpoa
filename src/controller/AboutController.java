package controller;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.awt.Desktop;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;

public class AboutController implements Initializable {

    @FXML
    public Hyperlink github;
    @FXML
    public Hyperlink waian;
    @FXML
    public Hyperlink nathan;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        github.setOnAction((e) -> {
            try {
                openWebpage(new URL("https://github.com/Waynocs/NathanWaian_cpoa"));
            } catch (MalformedURLException exc) {
            }
        });
        waian.setOnAction((e) -> {
            try {
                openWebpage(new URL("https://github.com/Waynocs"));
            } catch (MalformedURLException exc) {
            }
        });
        nathan.setOnAction((e) -> {
            try {
                openWebpage(new URL("https://github.com/WildGoat07"));
            } catch (MalformedURLException exc) {
            }
        });
    }

    // https://stackoverflow.com/a/10967469/13270517
    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // https://stackoverflow.com/a/10967469/13270517
    public static boolean openWebpage(URL url) {
        try {
            return openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }
}
