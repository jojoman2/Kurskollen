package Servletts;

import Beans.Teacher;
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


public class GetTeacherByStartingString extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String teacherStarting = req.getParameter("teacherStarting");
        if (!ErrorChecker.checkNotNull(new String[]{teacherStarting})) {
            resp.setStatus(400);
        } else {

            PrintWriter writer = resp.getWriter();

            try {
                Connection conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);
                List<Teacher> teachers = db.getTeachersByStartingString(teacherStarting);

                JSONArray teachersJSON = new JSONArray();
                for(Teacher teacher : teachers){
                    JSONObject teacherJSON = new JSONObject(teacher);
                    teacherJSON.remove("class");
                }

                writer.print(teachersJSON.toString());



            } catch (ClassNotFoundException e) {
                resp.setStatus(500);
                e.printStackTrace();
            } catch (SQLException e) {
                resp.setStatus(500);
                e.printStackTrace();
            }
        }

    }
}
