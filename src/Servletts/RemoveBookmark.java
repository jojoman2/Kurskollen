package Servletts;

import DatabaseStuff.DatabaseHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by fiona on 19/03/15.
 */
public class RemoveBookmark extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        Connection conn = null;
        PrintWriter = resp.getWriter();

        conn= DatabaseStuff.DbConnect.getConnection();
        DatabaseStuff.DatabaseHandler db = new DatabaseHandler(conn);
    }
}
