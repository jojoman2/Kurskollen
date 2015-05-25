package Servletts;

import DatabaseStuff.DatabaseHandler;
import utils.ErrorChecker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by fiona on 19/03/15.
 */
public class RemoveBookmark extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String username = req.getParameter("email");
        String loginSession = req.getParameter("loginsession");
        String courseIdString = req.getParameter("courseid");

        if (!ErrorChecker.checkNotNull(new String[]{courseIdString, username, loginSession})) {
            resp.setStatus(400);
        } else {




            try {

                Connection conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseHandler(conn);
                if(!db.checkLoginSession(username, loginSession)){
                    resp.setStatus(401);
                }
                else {
                    try {
                        db.removeBookmark(Integer.parseInt(courseIdString), username);
                        resp.setStatus(204);
                    } catch (NumberFormatException e) {
                        resp.setStatus(400);
                    }
                }


            } catch (ClassNotFoundException e) {
                resp.setStatus(500);
                e.printStackTrace();
            } catch (SQLException e) {
                resp.setStatus(500);
                e.printStackTrace();
            }
        }
    }
}
