package controller;

import javafx.scene.control.Alert.AlertType;
import javafx.util.Pair;

/**
 * Ahah fake classes are bad, I don't care. JAVA, GIVE US TYPEDEF PLEASE
 */
public class AlertPair extends Pair<String, AlertType> {

    public AlertPair(String arg0, AlertType arg1) {
        super(arg0, arg1);
    }

    public AlertPair() {
        this(null, null);
    }

    private static final long serialVersionUID = 1L;
}
