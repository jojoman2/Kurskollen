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
            if (ErrorChecker.validate(req.getParameter("email"))){
                email = req.getParameter("email");
            }else{

            }

            //check name doesn't exist in db
            if (name)

            try{
                db.addUser(email, );
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
