package Servletts;

import Beans.*;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

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
 * In: Coursename, school, distance
 * Out: Courses
 * public List<Course> searchForCourses(String name, int schoolid, String teacherName, boolean online)
 */


public class SearchForCourse extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Search for courses and return them

        resp.setContentType("application/json; charset=UTF-8");

        Connection conn = null;
        PrintWriter writer = resp.getWriter();
        try {
            conn = DatabaseStuff.DbConnect.getConnection();
            DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);


            String schoolidString = req.getParameter("schoolid");
            int schoolid = -1;
            if (schoolidString != null) {
                schoolid = Integer.parseInt(schoolidString);
            }

            String onlineString = req.getParameter("online");
            boolean online = onlineString != null && onlineString.equals("1");

            String pageNumberString = req.getParameter("page");
            int pageNumber;
            if (pageNumberString == null) {
                pageNumber = 1;
            }
            else{
                pageNumber = Integer.parseInt(pageNumberString);
                if(pageNumber<=0){
                    throw(new NumberFormatException("Page number lower than or equal to 0"));
                }
            }


            List<Course> courses = db.searchForCourses(req.getParameter("name"), schoolid, req.getParameter("teacher"), online , pageNumber);
            JSONArray coursesJson = new JSONArray();
            for (Course course : courses) {
                JSONObject courseJson = new JSONObject(course);
                courseJson.remove("class");
                coursesJson.put(courseJson);

                int courseId = course.courseId();

                //Get reviews
                List<Review> reviews = db.getReviewsByCourse(courseId);

                if (!reviews.isEmpty()) {

                    JSONArray reviewsJson = new JSONArray();
                    int sumOfReviewRatings = 0;
                    for (Review review : reviews) {
                        JSONObject jsonReview = new JSONObject(review);
                        jsonReview.remove("class");

                        //Get the name of the user
                        User postingUser = db.getUserInfo(review.userEmail());
                        jsonReview.put("user", postingUser.getName());

                        //Get the name of the teacher
                        int teacherId = review.teacherid();
                        Teacher teacher = db.getTeacherById(teacherId);
                        jsonReview.put("teacher", teacher.getName());


                        reviewsJson.put(jsonReview);
                        sumOfReviewRatings += review.getRating();
                    }


                    float meanReviewRating = ((float) sumOfReviewRatings) / reviews.size();
                    courseJson.put("meanRating", meanReviewRating);
                    courseJson.put("reviews", reviewsJson);
                }


                School school = db.getSchoolById(course.schoolId());
                courseJson.put("school", school.getName());
            }
            writer.print(coursesJson.toString());
        } catch(NumberFormatException e) {
            resp.setStatus(400);
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
