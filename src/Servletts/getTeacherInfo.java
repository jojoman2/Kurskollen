package Servletts;

import Beans.Course;
import Beans.Teacher;
import DatabaseStuff.DatabaseHandler;

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

        Connection conn  = null;
        PrintWriter writer = resp.getWriter();

        try {
            conn = DatabaseStuff.DbConnect.getConnection();
            DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);

            String teacherIdString = req.getParameter("teacherid");
            int teacherid = -1;
            if (teacherIdString != null){
                teacherid = Integer.parseInt(teacherIdString);
            }

            Teacher teacher = db.getTeacherById(teacherid);
            List<Course> courses = db.getCoursesByTeacher(teacherid);

            JSONObject teacherCourses = new JSONObject();
            teacherCourses.put(teacher);
            JSONArray courseJson = new JSONArray();
            for(Course course : courses){
                courseJson.put(new JSONObject(course));
            }
            teacherCourses.put(courseJson);

            writer.print(teacherCourses.toString());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
