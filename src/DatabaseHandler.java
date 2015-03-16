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

    public void addCourse(Course course){

    }

    public List<Course> searchForCourses(String name, int schoolid, int teacherId, boolean online){
        return null;
    }

    public List<Course> getCoursesByTeacher(int teacherId){
        return null;
    }


    //Bookmark

    public void addBookmark(int course, int userId){

    }

    public List<Course> listBookmarks(int userId){
        return null;
    }

    public void removeBookmark(int course, int userId){

    }

    //Review

    public void addReview(int courseId, Review review) throws SQLException {
        String query = "INSERT INTO reviews(rating, text, userid, courseid, teacherid)" +
                        " VALUES (?,?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, review.getRating() );


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
