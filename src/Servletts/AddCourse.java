package Servletts;

import Beans.Course;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Fredrik on 2015-03-19.
 */
public class AddCourse extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection conn = null;
        try {
            conn = DatabaseStuff.DbConnect.getConnection();
            DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);

            String creditsString = req.getParameter("credits");
            float credits = Float.parseFloat("creditsString");


            String schoolIdString = req.getParameter("schoolid");
            int schoolid = Integer.parseInt("schoolid");


            String onlineString = req.getParameter("online");
            boolean online = onlineString.equals("1");

            Course course =  new Course(req.getParameter("coursecode"), req.getParameter("name"), req.getParameter("description"), credits, online, req.getParameter("link"), schoolid);
            db.addCourse(course);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e){
            resp.setStatus(400);
        }

    }
}
