package Servletts;

import DatabaseStuff.DbConnect;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Fredrik on 2015-03-16.
 */
public class TestServlett extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            Connection conn = DbConnect.getConnection();
            PreparedStatement statement = conn.prepareStatement("" +
                    "INSERT INTO schools(name)" +
                    "VALUES(?)");
            statement.setString(1,"HO");
            statement.executeUpdate();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
