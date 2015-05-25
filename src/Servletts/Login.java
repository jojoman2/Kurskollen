package Servletts;

import utils.ErrorChecker;
import utils.General;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Fredrik on 2015-03-19.
 */
public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        PrintWriter writer = resp.getWriter();

        String username = req.getParameter("email");
        String password = req.getParameter("password");


        //Check if the login was correct and if so send a loginsession id to the client
        if (!ErrorChecker.checkNotNull(new String[]{username, password})) {
            resp.setStatus(400);
        }
        else {
            try {
                Connection conn  = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);

                boolean checkLogin = db.checkUser(username,password);


                if (checkLogin){
                    String loginsession = General.randomString(25);
                    writer.print(loginsession);
                    db.updateLoginSession(username, loginsession);
                } else{
                    writer.print("wrong-email-or-password");
                    resp.setStatus(401);
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
