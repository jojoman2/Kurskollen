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
        PrintWriter writer = resp.getWriter();


        //Check if the login was correct and if so send a loginsession id to the client
        if (!ErrorChecker.checkParameters(req, new String[]{"email", "name", "password"})) {
            resp.setStatus(400);
        }
        else {
            try {
                Connection conn  = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);

                boolean checkLogin = db.checkUser(req.getParameter("email"), req.getParameter("password"));
                boolean checkActivated = db.isActivated(req.getParameter("email"));
                if (checkLogin && checkActivated){
                    String loginsession = General.randomString(25);
                    writer.print(loginsession);
                    db.updateLoginSession(req.getParameter("email"), loginsession);
                }else if(checkLogin && !checkActivated){
                    writer.print("User not activated");
                    resp.setStatus(401);
                } else{
                    writer.print("Wrong password or email");
                    resp.setStatus(401);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
