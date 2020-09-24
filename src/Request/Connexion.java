package Request;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

    public static Connection creeConnexion() {
        // Pour se connecter à un serveur local

        String url = "jdbc:mysql://localhost/serano1u_pullmoche";
        url += "?serverTimezone=Europe/Paris";
        String login = "root";
        String pwd = "";

        /*
         * String url =
         * "jdbc:mysql://devbdd.iutmetz.univ-lorraine.fr:3306/serano1u_pullmoche"; url
         * += "?serverTimezone=Europe/Paris"; String login = "serano1u_appli"; String
         * pwd = "31904378";
         */

        Connection myConnection = null;
        try {
            myConnection = DriverManager.getConnection(url, login, pwd);
        } catch (SQLException sqle) {
            System.out.println("Connexion error" + sqle.getMessage());
        }

        return myConnection;
    }
}