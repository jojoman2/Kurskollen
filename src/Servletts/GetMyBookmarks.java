package Servletts;

import Beans.*;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
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
 * Created by Fredrik on 2015-04-24.
 */
public class GetMyBookmarks extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        if (!ErrorChecker.checkParameters(req, new String[]{"userEmail", "loginsession"})) {
            resp.setStatus(400);
        } else {
            PrintWriter writer = resp.getWriter();

            try {
                Connection conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);

                String userEmail = req.getParameter("userEmail");
                String loginsession = req.getParameter("loginsession");

                if(!db.checkLoginSession(userEmail,loginsession)) {
                    resp.setStatus(401);
                }
                else {

                    List<Course> courses = db.listBookmarks(userEmail);
                    JSONArray reviewsJson = new JSONArray();
                    for (Course course : courses) {
                        JSONObject courseJson = new JSONObject(course);
                        courseJson.remove("class");
                        reviewsJson.put(courseJson);

                        School school = db.getSchoolById(course.schoolId());
                        courseJson.put("school", school.getName());

                        List<Review> reviews = db.getReviewsByCourse(course.courseId());

                        if (!reviews.isEmpty()) {

                            int sumOfReviewRatings = 0;
                            for (Review review : reviews) {
                                sumOfReviewRatings += review.getRating();
                            }
                            float meanReviewRating = ((float) sumOfReviewRatings) / reviews.size();
                            courseJson.put("meanRating", meanReviewRating);
                        }
                    }

                    writer.print(reviewsJson.toString());
                }

            } catch (ClassNotFoundException e) {
                resp.setStatus(500);
                e.printStackTrace();
            } catch (SQLException e) {
                resp.setStatus(500);
                e.printStackTrace();
            } catch (JSONException e) {
                resp.setStatus(500);
                e.printStackTrace();
            }
        }
    }
}
