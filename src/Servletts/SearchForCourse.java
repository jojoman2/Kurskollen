package Servletts;

import Beans.Course;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
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

        Connection conn = null;
        PrintWriter writer = resp.getWriter();
        try {
            conn = DatabaseStuff.DbConnect.getConnection();
            DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);


            List<Course> courses = db.searchForCourses(req.getParameter("name"), Integer.parseInt(req.getParameter("schoolid")), req.getParameter("teacherName"), req.getParameter("online").equals("1"));
            JSONArray courseJson = new JSONArray();
            for(Course course : courses) {
                courseJson.put(new JSONObject(course));
            }
            writer.print(courseJson.toString());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
