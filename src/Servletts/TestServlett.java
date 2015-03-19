package Servletts;

import Beans.*;
import DatabaseStuff.DatabaseHandler;
import DatabaseStuff.DbConnect;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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


            //db.addReview(new Review(System.currentTimeMillis() / 1000L,3,"Lite sådär tyckte jag",1,1,1));
            List<Teacher> teachers = db.getTeachersByStartingString("Jar");
            for(Teacher teacher : teachers){
                writer.println(teacher.getName());
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
