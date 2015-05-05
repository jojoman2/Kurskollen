package Servletts;

import Beans.Review;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import utils.ErrorChecker;

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
public class GetCoursereviews extends HttpServlet {


    //NOT NEEDED ANY MORE!!! DO NOT USE

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String courseIdString = req.getParameter("courseid");

        if (!ErrorChecker.checkNotNull(new String[]{courseIdString})) {
            resp.setStatus(400);
        } else {

            PrintWriter writer = resp.getWriter();

            try {
                Connection conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);


                int courseid = Integer.parseInt(courseIdString);


                List<Review> reviews = db.getReviewsByCourse(courseid);
                System.out.print(reviews.size());
                JSONArray reviewsJson = new JSONArray();
                for (Review review : reviews) {
                    reviewsJson.put(new JSONObject(review));
                }
                writer.print(reviewsJson.toString());

            } catch (ClassNotFoundException e) {
                resp.setStatus(500);
                e.printStackTrace();
            } catch (SQLException e) {
                resp.setStatus(500);
                e.printStackTrace();
            } catch (NumberFormatException e) {
                resp.setStatus(400);
            }

        }


    }

}
