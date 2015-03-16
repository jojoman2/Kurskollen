package Servletts;

import DatabaseStuff.DatabaseHandler;
import DatabaseStuff.DbConnect;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
            PrintWriter writer = response.getWriter();
            Connection conn = DbConnect.getConnection();
            DatabaseHandler db = new DatabaseHandler(conn);
            //db.addUser("hej@hej.com","Arne","kalleanka","hsjdh4dh6");
            //writer.print(db.activateUser(3, "sjdfksjdf"));
            //writer.print(db.activateUser(3,"hsjdh4dh6"));
            writer.print(db.checkUser("hej@hej.com","kalleanka"));


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
