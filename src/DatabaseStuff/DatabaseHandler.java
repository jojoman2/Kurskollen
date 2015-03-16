package DatabaseStuff;

import Beans.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler {

    private Connection conn;

    public DatabaseHandler(Connection conn){
        this.conn = conn;
    }

    //User
    public void addUser(String email, String name, String password){
    }

    public boolean activateUser(int userId, String registrationCode){
        return false;
    }

    public boolean checkUser(String name, String password){
        return false;
    }

    public void changeUserDetails(String newName, String newPassword){

    }

    public User getUserInfo(int userid){
        return null;
    }

    //School

    public List<School> getSchools(){
        return null;
    }

   //Course

    public void addCourse(Course course) throws SQLException {
        String query =  "INSERT INTO courses(coursecode, name, description, credits, online, link, schoolid)" +
                        " VALUES(?,?,?,?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1,course.getCourseCode());
        stmt.setString(2, course.getName());
        stmt.setString(3, course.getDescription());
        stmt.setFloat(4, course.getCredits());
        stmt.setBoolean(5, course.isOnline());
        stmt.setString(6, course.getLink());
        stmt.setInt(7, course.getSchoolId());

        stmt.executeUpdate();
    }

    public List<Course> searchForCourses(String name, int schoolid, int teacherId, boolean online) throws SQLException {
        String query  ="SELECT * FROM courses WHERE" +
                        " (name = ? or ? is null)" +
                        " AND (schoolid = ? or ? is null)" +
                        " AND (teacherid = ? or ? is null)" +
                        " AND (online is ? or ? is null)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, name);
        stmt.setString(2, name);
        stmt.setInt(3, schoolid);
        stmt.setInt(4, schoolid);
        stmt.setInt(5, teacherId);
        stmt.setInt(6, teacherId);
        stmt.setBoolean(7, online);

        ResultSet results = stmt.executeQuery();

        List<Course> courses = new ArrayList<Course>();
        while(results.next()){
            Course course = new Course(results.getString("coursecode"), results.getString("name"), results.getString("description"), results.getFloat("credits"), results.getBoolean("online"), results.getString("link"), results.getInt("schoolid"));
            courses.add(course);
        }
        return courses;

    }

    public List<Course> getCoursesByTeacher(int teacherId) throws SQLException {
        String query  = "SELECT * FROM courses" +
                        " WHERE id = (SELECT courseid FROM teachesat WHERE teacherid = ?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, teacherId);
        ResultSet results = stmt.executeQuery();

        List<Course> courses = new ArrayList<Course>();
        while(results.next()){
            Course course = new Course(results.getString("coursecode"), results.getString("name"), results.getString("description"), results.getFloat("credits"), results.getBoolean("online"), results.getString("link"), results.getInt("schoolid"));
            courses.add(course);
        }
        return courses;

    }


    //Bookmark
    public void addBookmark(int course, int userId) throws SQLException {
        String query = "INSERT INTO savedcourse(courseid, userid)"+
                        " VALUES (?,?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, course);
        stmt.setInt(2, userId);
        stmt.executeUpdate();


    }

    public List<Course> listBookmarks(int userId) throws SQLException {
        String query = "SELECT * FROM savedcourse" +
                        " WHERE userid = ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);

        ResultSet results = stmt.executeQuery();

        List<Course> courses = new ArrayList<Course>();
        while(results.next()){
            Course course=  new Course(results.getString("coursecode"), results.getString("name"), results.getString("description"), results.getFloat("credits"),results.getBoolean("online"), results.getString("link"), results.getInt("schoolId"));
            courses.add(course);
        }
        return courses;


    }

    public void remmoveBookmark(int course, int userId) throws SQLException {
        String query = "DELETE FROM savedcourse" +
                        " WHERE courseid = ? AND userid = ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, course);
        stmt.setInt(2, userId);
        stmt.executeUpdate();


    }

    //Review
    public void addReview(int courseId, Review review) throws SQLException {
        String query = "INSERT INTO reviews(rating, text, userid, courseid, teacherid)" +
                        " VALUES (?,?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, review.getRating());
        stmt.setString(2, review.getText());
        stmt.setInt(3, review.getUserid());
        stmt.setInt(4, review.getCourseid());
        stmt.setInt(5, review.getTeacherid());

        stmt.executeUpdate();


    }


    //get reviews for a course
    public List<Review> getReviewsByCourse(int courseId) throws SQLException {
        String query =  "SELECT * FROM reviews" +
                        " WHERE courseid = ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, courseId);

        ResultSet results = stmt.executeQuery();

        List<Review> reviews = new ArrayList<Review>();
        while(results.next()){
            Review review  = new Review(results.getDate("time"),results.getInt("rating"), results.getString("text"), results.getInt("userint"), results.getInt("courseid"), results.getInt("teacherid"));
            reviews.add(review);
        }
        return reviews;


    }

    public List<Review> getReviewsByUser(int userId) throws SQLException {
        String query  =  "SELECT * FROM reviews" +
                        " WHERE userid = ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);

        ResultSet results = stmt.executeQuery();

        List<Review> reviews = new ArrayList<Review>();
        while(results.next()){
            Review review  = new Review(results.getDate("time"),results.getInt("rating"), results.getString("text"), results.getInt("userint"), results.getInt("courseid"), results.getInt("teacherid"));
            reviews.add(review);
        }
        return reviews;

    }


    //Teacher

    public void addTeacher(Teacher teacher) throws SQLException {

        String query = "INSERT INTO teacher(name)" +
                " VALUES (?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1,teacher.getName());
        stmt.executeUpdate();
    }


    public List<Teacher> getTeacherByCourse(int courseId) throws SQLException {
        String query = "SELECT name FROM teachers" +
                " WHERE id = " +
                 " (SELECT teacherid"+
                 " FROM teachesat WHERE courseid =?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, courseId);

        ResultSet results = stmt.executeQuery();

        List<Teacher> teachers = new ArrayList<Teacher>();
        while (results.next()){
            Teacher teacher = new Teacher(results.getString("name"));
            teachers.add(teacher);

        }
        return teachers;
    }

    public List<Teacher> getTeachersByStartingString(String startingString) throws SQLException {
        String query = "SELECT name FROM teachers" +
                        " WHERE name LIKE ?";

        PreparedStatement stmt =  conn.prepareStatement(query);
        stmt.setString(1,startingString);
        ResultSet results = stmt.executeQuery();

        List<Teacher> teachers = new ArrayList<Teacher>();
        while(results.next()){
            Teacher teacher  = new Teacher(results.getString("name"));
            teachers.add(teacher);
        }

        return teachers;

    }

}
