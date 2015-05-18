package Servletts;

import Beans.Course;
import Beans.Review;
import Beans.School;
import Beans.Teacher;
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
 * Created by Fredrik on 2015-03-19.
 */
public class GetMyReviews extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String userEmail = req.getParameter("email");
        String loginsession = req.getParameter("loginsession");

        if (!ErrorChecker.checkNotNull(new String[]{userEmail,loginsession})) {
            resp.setStatus(400);
        } else {
            PrintWriter writer = resp.getWriter();

            try {
                Connection conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);



                if(!db.checkLoginSession(userEmail,loginsession)) {
                    resp.setStatus(401);
                }
                else {

                    List<Review> reviews = db.getReviewsByUser(userEmail);
                    JSONArray reviewsJson = new JSONArray();
                    for (Review review : reviews) {
                        JSONObject reviewJson = new JSONObject(review);
                        reviewJson.remove("class");

                        Teacher teacher = db.getTeacherById(review.teacherid());
                        reviewJson.put("teacherId",teacher.getId());
                        reviewJson.put("teacherName",teacher.getName());

                        Course course = db.getCourseById(review.courseid());
                        reviewJson.put("courseName", course.getName());

                        School school = db.getSchoolById(course.schoolId());
                        reviewJson.put("schoolName",school.getName());

                        reviewsJson.put(reviewJson);
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
