package Servletts;

import DatabaseStuff.DatabaseHandler;
import utils.ErrorChecker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class RemoveReview extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String email = req.getParameter("email");
        String loginSession = req.getParameter("loginsession");
        String reviewIdString = req.getParameter("reviewId");

        if (!ErrorChecker.checkNotNull(new String[]{reviewIdString, email, loginSession})) {
            resp.setStatus(400);
        } else {
            try {

                int reviewId = Integer.parseInt(reviewIdString);

                Connection conn = DatabaseStuff.DbConnect.getConnection();
                DatabaseStuff.DatabaseHandler db = new DatabaseHandler(conn);
                if(!db.checkLoginSession(email, loginSession)){
                    resp.setStatus(401);
                }
                else {
                    String reviewPoster = db.getReviewPosterById(reviewId);
                    if(reviewPoster == null){
                        resp.setStatus(400);
                    }
                    else if(!reviewPoster.equals(email)){
                        resp.setStatus(401);
                    }
                    else {
                        db.removeReview(reviewId);
                        resp.setStatus(204);
                    }
                }


            } catch (ClassNotFoundException e) {
                resp.setStatus(500);
                e.printStackTrace();
            } catch (NumberFormatException e){
                resp.setStatus(400);
                e.printStackTrace();
            } catch (SQLException e) {
                resp.setStatus(500);
                e.printStackTrace();
            }
        }
    }

}
