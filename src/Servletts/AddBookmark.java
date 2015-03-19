package Servletts;

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
public class AddBookmark extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection conn = null;
        PrintWriter writer  = resp.getWriter();

        try {
            conn = DatabaseStuff.DbConnect.getConnection();
            DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);


            db.addBookmark(Integer.parseInt(req.getParameter("courseid")), Integer.parseInt(req.getParameter("userid")));




        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
