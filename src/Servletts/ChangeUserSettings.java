package Servletts;

import utils.ErrorChecker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


public class ChangeUserSettings extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String username = req.getParameter("email");
        String loginSession = req.getParameter("loginsession");
        String newName = req.getParameter("newName");
        String newPassword = req.getParameter("newPassword");


        if (!ErrorChecker.checkNotNull(new String[]{username, loginSession})) {
            resp.setStatus(400);
        }
        else {


            try {
                Connection conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);



                if (!db.checkLoginSession(username, loginSession)) {
                    resp.setStatus(401);
                }
                else {
                    db.changeUserDetails(username, newName, newPassword);
                    resp.setStatus(200);
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
