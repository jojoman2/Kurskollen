package Servletts;

import Beans.Review;

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
public class CreateReview extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter writer = resp.getWriter();

        try {
            Connection conn = DatabaseStuff.DbConnect.getConnection();
            DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);

            String ratingString = req.getParameter("rating");
            int rating = -1;
            if (ratingString != null){
                rating = Integer.parseInt(ratingString);
            }

            String courseIdString = req.getParameter("courseid");
            int courseid = -1;
            if (courseIdString!= null){
                courseid  = Integer.parseInt(courseIdString);
            }

            String teacherIdString = req.getParameter("teacherid");
            int teacherid = -1;
            if (teacherIdString!= null){
                teacherid  = Integer.parseInt(teacherIdString);
            }

            Review review = new Review(System.currentTimeMillis()/1000, rating , req.getParameter("text"), req.getParameter("useremail"),courseid, teacherid);
            db.addReview(review);
            resp.setStatus(201);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(422);
        }


    }
}
