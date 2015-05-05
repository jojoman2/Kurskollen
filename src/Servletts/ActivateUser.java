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
 * Created by fiona on 19/03/15.
 */
public class ActivateUser extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String email = req.getParameter("email");
        String activationCode = req.getParameter("activationcode");

        if (!ErrorChecker.checkNotNull(new String[]{email, activationCode})) {
            resp.setStatus(400);
        } else {

            try {
                Connection conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);

                boolean activate = db.activateUser(email, activationCode);
                if (!activate) {
                    resp.setStatus(401);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                resp.setStatus(500);
            } catch (SQLException e) {
                e.printStackTrace();
                resp.setStatus(500);
            }


        }
    }
}
