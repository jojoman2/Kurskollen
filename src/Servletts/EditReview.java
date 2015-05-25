package Servletts;

import Beans.Review;
import Beans.Teacher;
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
public class EditReview extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain; charset=UTF-8");

        String reviewIdString = req.getParameter("reviewId");
        String username = req.getParameter("email");
        String loginSession = req.getParameter("loginsession");
        String ratingString = req.getParameter("rating");
        String courseIdString = req.getParameter("courseid");
        String teacherName = req.getParameter("teacher");
        String text = req.getParameter("text");


        if (!ErrorChecker.checkNotNull(new String[]{reviewIdString,username,loginSession,ratingString, courseIdString,teacherName,text})) {
            resp.setStatus(400);
        } else {

            try {
                Connection conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseStuff.DatabaseHandler(conn);


                if(!db.checkLoginSession(username, loginSession)){
                    resp.setStatus(401);
                }
                else {

                    Teacher teacher = db.getTeacherByName(teacherName);
                    int teacherId;
                    if(teacher != null){
                        teacherId = teacher.getId();
                    }
                    else{
                        teacherId = db.addTeacher(new Teacher(teacherName));
                    }
                    


                    int reviewId = Integer.parseInt(reviewIdString);
                    int rating = Integer.parseInt(ratingString);
                    int courseid = Integer.parseInt(courseIdString);



                    if(rating>5||rating<0){
                        resp.setStatus(400);
                    }
                    else {
                        String reviewPoster = db.getReviewPosterById(reviewId);
                        if(reviewPoster == null){
                            resp.setStatus(400);
                        }
                        else if(!reviewPoster.equals(username)){
                            resp.setStatus(401);
                        }
                        else {
                            Review review = new Review(System.currentTimeMillis() / 1000, rating, text, username, courseid, teacherId);
                            db.editReview(reviewId, review);
                            resp.setStatus(201);
                        }
                    }
                }


            } catch (ClassNotFoundException e) {
                resp.setStatus(500);
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
                resp.setStatus(500);
            } catch (NumberFormatException e){
                resp.setStatus(400);
            }


        }
    }
}
