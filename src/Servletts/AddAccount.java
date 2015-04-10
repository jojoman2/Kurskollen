package Servletts;

import DatabaseStuff.DatabaseHandler;
import utils.EmailSender;
import utils.ErrorChecker;
import utils.General;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;


/*
    In parameters:
        Email
        Username
        Password*/
public class AddAccount extends HttpServlet {

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain; charset=UTF-8");
        PrintWriter writer = resp.getWriter();

        if (!ErrorChecker.checkParameters(req, new String[]{"email", "name", "password"})) {
            resp.setStatus(400);
            //writer.print(Boolean.toString(req.getParameter("email") == null)+" "+Boolean.toString(req.getParameter("name") == null)+" "+Boolean.toString(req.getParameter("password") == null));
        }
        else {

            Connection conn = null;


            try {
                conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseHandler(conn);


                String name = req.getParameter("name");

                String email = req.getParameter("email");
                String password = req.getParameter("password");



                boolean addAccount = true;
                if (!ErrorChecker.validateEmail(email)) {
                    addAccount = false;
                    writer.print("emailaddress-wrong");
                }

                if(!ErrorChecker.valadiatePassword(password)){
                    addAccount = false;
                    writer.print("password-wrong");
                }

                if (addAccount) {
                    //generate activation code
                    String activationCode = General.randomString(20);

                    db.addUser(email, name, password, activationCode);
                    EmailSender.sendEmail(email, name, activationCode);
                    resp.setStatus(201);

                } else {
                    resp.setStatus(400);

                }


            } catch (ClassNotFoundException e) {
                resp.setStatus(500);
                e.printStackTrace();
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    writer.print("user-or-email-exists");
                    resp.setStatus(400);
                } else {
                    resp.setStatus(500);
                    e.printStackTrace();
                }

            }

        }
    }
}
