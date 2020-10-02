package request;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

import controller.INIReader;

/**
 * Object used to manage an SQL connection
 */
public class Connection {

    private static Connection instance;
    private java.sql.Connection connection;

    private Connection() {
        creeConnexion();
    }

    /**
     * Returns the active connection
     * 
     * @return the active connection
     * @throws SQLException
     */
    public static java.sql.Connection getConnection() throws SQLException {
        if (instance == null)
            instance = new Connection();
        if (instance.connection.isClosed())
            instance.creeConnexion();
        return instance.connection;
    }

    private void creeConnexion() {
        // Pour se connecter à un serveur local
        var connectionInfo = INIReader.ReadFromFile(new File("ConnectionInfo.ini")).get("Connection");
        System.out.println("Connecting to " + connectionInfo.get("uri"));
        try {
            connection = DriverManager.getConnection(connectionInfo.get("uri"), connectionInfo.get("login"),
                    connectionInfo.get("pwd"));
        } catch (SQLException sqle) {
            System.out.println("Connexion error" + sqle.getMessage());
        }
    }
}