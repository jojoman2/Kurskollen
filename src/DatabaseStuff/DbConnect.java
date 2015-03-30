package DatabaseStuff;

import com.google.appengine.api.utils.SystemProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Fredrik on 2015-03-16.
 */
public class DbConnect {

    private DbConnect(){}

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        String url = null;
        if (SystemProperty.environment.value() ==
                SystemProperty.Environment.Value.Production) {
            // Load the class that provides the new "jdbc:google:mysql://" prefix.
            Class.forName("com.mysql.jdbc.GoogleDriver");
            url = "jdbc:google:mysql://kurskollenapp:kurskollensql/kurskollen?user=root";
        } else {
            // Local MySQL instance to use during development.
            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://localhost:3306/kurskollen?user=root";

        }
        return DriverManager.getConnection(url);
    }
}
