package Servletts;

import Beans.Course;
import Beans.School;
import Beans.User;
import DatabaseStuff.DatabaseHandler;
import DatabaseStuff.DbConnect;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Fredrik on 2015-03-16.
 */
public class TestServlett extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response){

        try {
            PrintWriter writer = response.getWriter();
            Connection conn = DbConnect.getConnection();
            DatabaseHandler db = new DatabaseHandler(conn);
            //db.addUser("hej@hej.com","Arne","kalleanka","hsjdh4dh6");
            //writer.print(db.activateUser(3, "sjdfksjdf"));
            //writer.print(db.activateUser(3,"hsjdh4dh6"));
            //writer.print(db.checkUser("hej@hej.com","kalleanka"));
            //db.changeUserDetails(1,null,"kalleanka");
            //writer.println(db.checkUser("fredde_wallen@hotmail.com","kalleanka"));
            /*List<School> schools = db.getSchools();
            for(School school : schools){
                writer.println(school.getName());
            }*/
            //db.addCourse(new Course("DD2385","Programutvecklingsteknik","En fortsättningskurs i datalogi",6,false,"www.kth.se/student/kurser/kurs/DD2385?l=sv",1));
            List<Course> courses = db.searchForCourses("interak",1,"Ste",false);
            for(Course course : courses){
                writer.println(course.getName());
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
