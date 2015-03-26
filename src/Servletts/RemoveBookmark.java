package Servletts;

import DatabaseStuff.DatabaseHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by fiona on 19/03/15.
 */
public class RemoveBookmark extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        Connection conn = null;


        try {
            conn= DatabaseStuff.DbConnect.getConnection();
            DatabaseStuff.DatabaseHandler db = new DatabaseHandler(conn);

            try{
                db.removeBookmark(Integer.parseInt(req.getParameter("courseid")), req.getParameter("useremail"));
            }
            catch(NumberFormatException e){
                resp.setStatus(400);
            }



        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
