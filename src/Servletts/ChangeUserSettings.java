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


            Connection conn = null;
            try {
                conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);

                db.changeUserDetails(req.getParameter("email"), req.getParameter("newName"), req.getParameter("newPassword"));
                resp.setStatus(200);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


}
