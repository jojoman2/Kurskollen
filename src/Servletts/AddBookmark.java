package Servletts;

import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by Fredrik on 2015-03-19.
 */
public class AddBookmark extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection conn;



        try {

            conn = DatabaseStuff.DbConnect.getConnection();
            DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);

            try{
                int couseid = Integer.parseInt(req.getParameter("courseid"));
                int userid = Integer.parseInt(req.getParameter("userid"));
                db.addBookmark(couseid, userid);
            } catch (NumberFormatException e){
                resp.setStatus(400);
                throw(e);
            }








        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
