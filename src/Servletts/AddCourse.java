package Servletts;

import Beans.Course;
import utils.Constants;
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

        resp.setContentType("application/json; charset=UTF-8");

        String username = req.getParameter("email");
        String loginsession = req.getParameter("loginsession");
        String creditsString = req.getParameter("credits");
        String schoolIdString = req.getParameter("schoolid");
        String courseCode = req.getParameter("coursecode");
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String link = req.getParameter("link");
        String onlineString = req.getParameter("online");

        if (!ErrorChecker.checkNotNull(new String[]{username,loginsession,courseCode, name, creditsString, onlineString, schoolIdString })) {
            resp.setStatus(400);
        } else {

            try {
                Connection conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);



                if(!db.checkLoginSession(username,loginsession)) {
                    resp.setStatus(401);
                }
                else{

                    float credits = Float.parseFloat(creditsString);



                    int schoolid = Integer.parseInt(schoolIdString);

                    if(Constants.schools.get(schoolid) == null){
                        resp.setStatus(400);
                    }
                    else {

                        boolean online = onlineString.equals("1");

                        Course course = new Course(courseCode, name, description, credits, online, link, schoolid);
                        db.addCourse(course);
                    }
                }


            } catch (ClassNotFoundException e) {
                resp.setStatus(500);
                e.printStackTrace();
            } catch (SQLException e) {
                resp.setStatus(500);
                e.printStackTrace();
            } catch (NumberFormatException e){
                resp.setStatus(400);
                e.printStackTrace();
            }

         }
    }

}
