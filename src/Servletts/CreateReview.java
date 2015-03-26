package Servletts;

import Beans.Review;
import utils.ErrorChecker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Fredrik on 2015-03-19.
 */
public class CreateReview extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!ErrorChecker.checkParameters(req, new String[]{"email","loginsession","rating", "courseid","teacherid","text"})) {
            resp.setStatus(400);
        } else {

            try {
                Connection conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);

                String email = req.getParameter("email");
                String loginSession = req.getParameter("loginsession");

                if(!db.checkLoginSession(email, loginSession)){
                    resp.setStatus(401);
                }
                else {

                    String ratingString = req.getParameter("rating");
                    String courseIdString = req.getParameter("courseid");
                    String teacherIdString = req.getParameter("teacherid");

                    int rating = Integer.parseInt(ratingString);
                    int courseid = Integer.parseInt(courseIdString);
                    int teacherid = Integer.parseInt(teacherIdString);

                    if(rating>5||rating<0){
                        resp.setStatus(400);
                    }
                    else {
                        Review review = new Review(System.currentTimeMillis() / 1000, rating, req.getParameter("text"), req.getParameter("useremail"), courseid, teacherid);
                        db.addReview(review);
                        resp.setStatus(201);
                    }
                }


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
                resp.setStatus(400);
            } catch (NumberFormatException e){
                resp.setStatus(400);
            }


        }
    }
}
