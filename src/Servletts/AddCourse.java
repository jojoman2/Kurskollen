package Servletts;

import Beans.Course;
import utils.ErrorChecker;

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

        if (!ErrorChecker.checkParameters(req, new String[]{"email","loginsession","coursecode", "name", "description", "credits", "online", "link", "schoolid" })) {
            resp.setStatus(400);
        } else {

            try {
                Connection conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);

                String email = req.getParameter("email");
                String loginsession = req.getParameter("loginsession");

                if(!db.checkLoginSession(email,loginsession)) {
                    resp.setStatus(401);
                }
                else{
                    String creditsString = req.getParameter("credits");
                    float credits = Float.parseFloat("creditsString");


                    String schoolIdString = req.getParameter("schoolid");
                    int schoolid = Integer.parseInt("schoolid");


                    String onlineString = req.getParameter("online");
                    boolean online = onlineString.equals("1");

                    Course course = new Course(req.getParameter("coursecode"), req.getParameter("name"), req.getParameter("description"), credits, online, req.getParameter("link"), schoolid);
                    db.addCourse(course);
                }


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e){
                resp.setStatus(400);
            }

         }
    }

}
