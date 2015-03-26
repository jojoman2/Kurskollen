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
import java.util.List;

/**
 * Created by Fredrik on 2015-03-19.
 */
public class GetCourseReviews extends HttpServlet {


    //TEST ME PLZ!!!!!

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = null;
        PrintWriter writer  = resp.getWriter();

        try {
            conn = DatabaseStuff.DbConnect.getConnection();
            DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);

            String courseIdString = req.getParameter("courseid");
            int courseid = -1;
            if (courseIdString != null){
                courseid = Integer.parseInt(courseIdString);
            }

            List<Review> reviews = db.getReviewsByCourse(courseid);
            JSONArray reviewsJson =  new JSONArray();
            for (Review review : reviews){
                reviewsJson.put(new JSONObject(review));
            }
            writer.print(reviewsJson.toString());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }




    }

}
