package Servletts;

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
            float credits = -1;
            if (creditsString != null){
                credits  = Float.parseFloat("creditsString");
            }

            String schoolIdString = req.getParameter("schoolid");
            schoolid = Integer.parseInt("schoolid");


            String

            Course course =  new Course(req.getParameter("coursecode"), req.getParameter("name"), req.getParameter("description"), credits, )
            db.addCourse(course);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
