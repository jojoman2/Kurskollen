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
        if (!ErrorChecker.checkParameters(req, new String[]{"courseid", "useremail", "loginsession"})) {
            resp.setStatus(400);
        } else {

            String email = req.getParameter("useremail");
            String loginSession = req.getParameter("loginsession");


            try {

                Connection conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseHandler(conn);
                if(!db.checkLoginSession(email, loginSession)){
                    resp.setStatus(401);
                }
                else {
                    try {
                        db.removeBookmark(Integer.parseInt(req.getParameter("courseid")), req.getParameter("useremail"));
                        resp.setStatus(204);
                    } catch (NumberFormatException e) {
                        resp.setStatus(400);
                    }
                }


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
