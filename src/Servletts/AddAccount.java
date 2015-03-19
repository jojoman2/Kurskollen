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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;


/*
    In parameters:
        Email
        Username
        Password
    Out:
        Json:
            boolean success
 asdasasd*/
public class AddAccount extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection conn =  null;
        PrintWriter writer = resp.getWriter();

        try {
            conn = DatabaseStuff.DbConnect.getConnection();
            DatabaseStuff.DatabaseHandler db = new DatabaseHandler(conn);


            String email = null;
            String name = null;
            Boolean addAccount =true;
            String emailString = req.getParameter("email");
            String nameString = req.getParameter("name");

            if (!db.checkEmailUnique(emailString)){
                addAccount = false;
            }

            if (ErrorChecker.validate(emailString)){
                email = req.getParameter("email");
            }else{
                addAccount = false;
            }

            //check name doesn't exist in db
            if (!db.checkNameUnique("name")){
                addAccount = false;
            }else{
                name = req.getParameter("name");
            }

            //generate activation code
            String activationCode = General.randomString(25);



            try{
                db.addUser(email, name, req.getParameter("password"), activationCode);

                EmailSender.sendEmail(email, name, activationCode);

            }catch(NumberFormatException e){
                resp.setStatus(400);
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
