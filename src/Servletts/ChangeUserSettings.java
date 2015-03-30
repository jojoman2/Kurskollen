package Servletts;

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
public class ChangeUserSettings extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        if (!ErrorChecker.checkParameters(req, new String[]{"email", "loginsession"})) {
            resp.setStatus(400);
        }
        else {

            Connection conn = null;
            try {
                conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);

                String email = req.getParameter("email");
                String loginSession = req.getParameter("loginsession");

                if (!db.checkLoginSession(email, loginSession)) {
                    resp.setStatus(401);
                }
                else {
                    db.changeUserDetails(email, req.getParameter("newName"), req.getParameter("newPassword"));
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
