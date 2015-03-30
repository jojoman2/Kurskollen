package Servletts;

import utils.ErrorChecker;

import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by Fredrik on 2015-03-19.
 */
public class AddBookmark extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection conn;
        PrintWriter writer = resp.getWriter();

        if (!ErrorChecker.checkParameters(req, new String[]{"email", "loginsession", "courseid"})) {
            resp.setStatus(400);
        } else {

            String email = req.getParameter("email");
            String loginSession = req.getParameter("loginsession");

            try {

                conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);
                if(!db.checkLoginSession(email, loginSession)){
                    resp.setStatus(401);
                }
                else {
                    try {
                        int couseid = Integer.parseInt(req.getParameter("courseid"));
                        db.addBookmark(couseid, email);
                        resp.setStatus(201);
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
