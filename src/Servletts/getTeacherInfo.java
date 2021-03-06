package Servletts;

import Beans.Course;
import Beans.Teacher;
import DatabaseStuff.DatabaseHandler;
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


public class GetTeacherInfo extends HttpServlet {

    //TEST ME PLZ!!!!!


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");
        String teacherIdString = req.getParameter("teacherid");

        if (!ErrorChecker.checkNotNull(new String[]{teacherIdString})) {
            resp.setStatus(400);
        } else {

            PrintWriter writer = resp.getWriter();

            try {
                Connection conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);


                int teacherid = Integer.parseInt(teacherIdString);


                Teacher teacher = db.getTeacherById(teacherid);
                if(teacher == null){
                    resp.setStatus(400);
                }
                else {
                    List<Course> courses = db.getCoursesByTeacher(teacherid);

                    JSONObject teacherCourses = new JSONObject();

                    teacherCourses.put("name", teacher.getName());
                    JSONArray courseJson = new JSONArray();
                    for (Course course : courses) {
                        courseJson.put(new JSONObject(course));
                    }
                    teacherCourses.put("courses", courseJson);

                    writer.print(teacherCourses.toString());
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
            } catch (NumberFormatException e) {
                resp.setStatus(400);
            }
        }
    }
}
