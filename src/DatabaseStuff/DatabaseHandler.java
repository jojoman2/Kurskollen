package DatabaseStuff;

import Beans.*;
import org.mindrot.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//dsdsdfsdsdf
public class DatabaseHandler {

    private static final int RESULTS_PER_PAGE = 50;

    private Connection conn;

    public DatabaseHandler(Connection conn){
        this.conn = conn;
    }

    //User

    public void addUser(String username, String name, String password) throws SQLException {
        String hashedPassword = BCrypt.hashpw(password,BCrypt.gensalt());
        String query =
                "INSERT INTO users(username,name,passwordhash)" +
                " VALUES(?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1,username);
        stmt.setString(2,name);
        stmt.setString(3,hashedPassword);
        stmt.executeUpdate();
    }

    public boolean checkUser(String username, String password) throws SQLException {
        String query =
                "SELECT passwordhash" +
                " FROM users" +
                " WHERE username=?" +
                " LIMIT 1";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1,username);
        ResultSet result = stmt.executeQuery();
        if(!result.next()){
            return false;
        }
        String hashedPassword = result.getString("passwordhash");
        return BCrypt.checkpw(password,hashedPassword);
    }

    public void changeUserDetails(String username, String newName, String newPassword) throws SQLException {
        if(newName!=null) {
            String query =
                    "UPDATE users" +
                    " SET name=?"+
                    " WHERE username=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,newName);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
        if(newPassword!=null){
            String passwordHash = BCrypt.hashpw(newPassword,BCrypt.gensalt());
            String query =
                    "UPDATE users" +
                    " SET passwordhash=?"+
                    " WHERE username=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,passwordHash);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public User getUserInfo(String username) throws SQLException {
        String query =
                "SELECT name" +
                " FROM users"+
                " WHERE username=?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet result = stmt.executeQuery();
        result.next();
        return new User(result.getString("name"),username);
    }

    //Checks if user loginsession equals login session
    public boolean checkLoginSession(String username, String loginSession) throws SQLException {
        String query =
                "SELECT loginsession FROM users"+
                " WHERE username=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);

        ResultSet result =stmt.executeQuery();
        if(!result.next()){
            return false;
        }

        String storedLogin = result.getString("loginsession");

        //Compares in a way which avoids attacks measuring comparation time
        if(loginSession.length()!=storedLogin.length()){
            return false;
        }
        boolean sameString = true;
        for(int i = 0;i<(loginSession.length());i++){
            if(loginSession.charAt(i)!=storedLogin.charAt(i)){
                sameString=false;
            }
        }
        return sameString;
    }


    //lägga in loginsessionide
    public void updateLoginSession(String username, String loginSession) throws SQLException {
        String query =
                "UPDATE users"+
                " SET loginsession = ?"+
                " WHERE username = ?";


        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, loginSession);
        stmt.setString(2, username);
        stmt.executeUpdate();


    }

    //School

    public List<School> getSchools() throws SQLException {
        String query =
                "SELECT name" +
                " FROM schools";
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery(query);
        List<School> schools = new ArrayList<School>();
        while(results.next()){
            School review  = new School(results.getString("name"));
            schools.add(review);
        }
        return schools;



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
        stmt.setInt(7, course.schoolId());

        stmt.executeUpdate();
    }

    public List<Course> searchForCourses(String name, int schoolid, String teacherName, boolean online, int pageNumber) throws SQLException {
        int startingLimit = (pageNumber-1)*RESULTS_PER_PAGE;
        int endingLimit = pageNumber*RESULTS_PER_PAGE-1;
        String query  ="SELECT * FROM courses " +
                        " WHERE" +
                            " (name LIKE ? or ? is null)" +
                            " AND (schoolid = ? or ?=-1)" +
                            " AND (id IN " +
                                " (SELECT courseid FROM teachesat WHERE teacherid IN " +
                                    " (SELECT id FROM teachers WHERE name LIKE ?)) or ? is null)" +
                            " AND (online = ?)" +
                        " ORDER BY id"+
                        " LIMIT "+startingLimit+","+ endingLimit;
        PreparedStatement stmt = conn.prepareStatement(query);
        if(name!=null) {
            stmt.setString(1, '%' + name + '%');
        }
        else{
            stmt.setString(1,null);
        }
        stmt.setString(2, name);
        stmt.setInt(3, schoolid);
        stmt.setInt(4, schoolid);
        if(teacherName!=null) {
            stmt.setString(5, '%' + teacherName + '%');
        }
        else{
            stmt.setString(5, null);
        }
        stmt.setString(6, teacherName);
        stmt.setBoolean(7, online);

        ResultSet results = stmt.executeQuery();

        List<Course> courses = new ArrayList<Course>();
        while(results.next()){
            Course course = new Course(results.getInt("id"),results.getString("coursecode"), results.getString("name"), results.getString("description"), results.getFloat("credits"), results.getBoolean("online"), results.getString("link"), results.getInt("schoolid"));
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

    public Course getCourseById(int id) throws SQLException {
        String query = "SELECT * FROM courses" +
                " WHERE id=?" +
                " LIMIT 1";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);

        ResultSet results = stmt.executeQuery();
        results.next();

        return new Course(results.getString("coursecode"), results.getString("name"), results.getString("description"), results.getFloat("credits"), results.getBoolean("online"), results.getString("link"), results.getInt("schoolid"));

    }



    //Bookmark
    public void addBookmark(int course, String userEmail) throws SQLException {
        String query = "INSERT INTO savedcourse(courseid, useremail)"+
                        " VALUES (?,?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, course);
        stmt.setString(2, userEmail);
        stmt.executeUpdate();


    }

    public List<Course> listBookmarks(String userEmail) throws SQLException {
        String query = "SELECT * FROM courses WHERE id IN (SELECT courseid FROM savedcourse" +
                        " WHERE useremail = ?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, userEmail);

        ResultSet results = stmt.executeQuery();

        List<Course> courses = new ArrayList<Course>();
        while(results.next()){
            Course course=  new Course(results.getInt("id"),results.getString("coursecode"), results.getString("name"), results.getString("description"), results.getFloat("credits"),results.getBoolean("online"), results.getString("link"), results.getInt("schoolId"));
            courses.add(course);
        }
        return courses;


    }

    public void removeBookmark(int course, String userEmail) throws SQLException {
        String query = "DELETE FROM savedcourse" +
                        " WHERE courseid = ? AND useremail = ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, course);
        stmt.setString(2, userEmail);
        stmt.executeUpdate();


    }

    //Review
    public void addReview(Review review) throws SQLException {
        String query = "INSERT INTO reviews(rating,time, text, username, courseid, teacherid)" +
                        " VALUES (?,?,?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, review.getRating());
        stmt.setLong(2, review.getTime());
        stmt.setString(3, review.getText());
        stmt.setString(4, review.userEmail());
        stmt.setInt(5, review.courseid());
        stmt.setInt(6, review.teacherid());

        stmt.executeUpdate();


    }

    public void editReview(int reviewId, Review review) throws SQLException {
        String query = "" +
                "UPDATE reviews" +
                " SET rating=?, time=?, text=?, username=?, courseid=?, teacherid=?" +
                " WHERE id=?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1,review.getRating());
        stmt.setLong(2, review.getTime());
        stmt.setString(3, review.getText());
        stmt.setString(4, review.userEmail());
        stmt.setInt(5, review.courseid());
        stmt.setInt(6, review.teacherid());
        stmt.setInt(7, reviewId);

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
            Review review  = new Review(results.getLong("time"),results.getInt("rating"), results.getString("text"), results.getString("useremail"), results.getInt("courseid"), results.getInt("teacherid"));
            reviews.add(review);
        }
        return reviews;
    }

    public List<Review> getReviewsByUser(String username) throws SQLException {
        String query  =  "SELECT * FROM reviews" +
                        " WHERE username = ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);

        ResultSet results = stmt.executeQuery();

        List<Review> reviews = new ArrayList<Review>();
        while(results.next()){
            Review review  = new Review(results.getLong("time"),results.getInt("rating"), results.getString("text"), results.getString("useremail"), results.getInt("courseid"), results.getInt("teacherid"));
            reviews.add(review);
        }
        return reviews;

    }

    public String getReviewPosterById(int id) throws SQLException {
        String query = "" +
                "SELECT username" +
                " FROM reviews" +
                " WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1,id);
        ResultSet result = stmt.executeQuery();
        if(!result.next()){
            return null;
        }
        return result.getString("useremail");
    }

    public void removeReview(int reviewId) throws SQLException {
        String query = "" +
                "DELETE FROM reviews" +
                " WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1,reviewId);

        stmt.executeUpdate();
    }


    //Teacher

    public void addTeacher(Teacher teacher, int courseid) throws SQLException {

        String query = "INSERT INTO teachers(name)" +
                " VALUES (?)";

        PreparedStatement stmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, teacher.getName());
        stmt.executeUpdate();
        ResultSet result = stmt.getGeneratedKeys();
        result.next();
        int id = result.getInt(1);


        String query2 = "INSERT INTO teachesat(courseid, teacherid)" +
                " VALUES(?,?)";
        PreparedStatement stmt2 = conn.prepareStatement(query2);
        stmt2.setInt(1,courseid);
        stmt2.setInt(2, id);
        stmt2.executeUpdate();
    }


    public List<Teacher> getTeacherByCourse(int courseId) throws SQLException {
        String query = "SELECT id,name FROM teachers" +
                " WHERE id = " +
                 " (SELECT teacherid"+
                 " FROM teachesat WHERE courseid =?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, courseId);

        ResultSet results = stmt.executeQuery();

        List<Teacher> teachers = new ArrayList<Teacher>();
        while (results.next()){
            Teacher teacher = new Teacher(results.getInt("id"),results.getString("name"));
            teachers.add(teacher);

        }
        return teachers;
    }

    public List<Teacher> getTeachersByStartingString(String startingString) throws SQLException {
        String query = "SELECT id,name FROM teachers" +
                        " WHERE name LIKE ?";

        PreparedStatement stmt =  conn.prepareStatement(query);
        stmt.setString(1, startingString + '%');
        ResultSet results = stmt.executeQuery();

        List<Teacher> teachers = new ArrayList<Teacher>();
        while(results.next()){
            Teacher teacher  = new Teacher(results.getInt("id"),results.getString("name"));
            teachers.add(teacher);
        }

        return teachers;

    }

    public Teacher getTeacherById(int id) throws SQLException{
        String query = "SELECT *" +
                " FROM teachers" +
                " WHERE id=?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet result = stmt.executeQuery();
        if(!result.next()){
            return null;
        }
        return new Teacher(result.getInt("id"), result.getString("name"));
    }

    //School
    public School getSchoolById(int id) throws SQLException{
        String query =
                "SELECT *" +
                " FROM schools" +
                " WHERE id=?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet result = stmt.executeQuery();
        result.next();
        return new School(result.getString("name"));
    }


}
