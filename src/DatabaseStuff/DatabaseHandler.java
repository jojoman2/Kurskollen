package databaseStuff;

import beans.*;
import org.mindrot.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler {

    private Connection conn;

    public DatabaseHandler(Connection conn){
        this.conn = conn;
    }

    //User

    public void addUser(String email, String name, String password, String activationCode) throws SQLException {
        String hashedPassword = BCrypt.hashpw(password,BCrypt.gensalt());
        String query =
                "INSERT INTO users(email,name,passwordhash,activationcode)" +
                " VALUES(?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1,email);
        stmt.setString(2,name);
        stmt.setString(3,hashedPassword);
        stmt.setString(4,activationCode);
        stmt.executeUpdate();
    }

    public boolean activateUser(int userId, String enteredActivationCode) throws SQLException {
        String selectQuery =
                "SELECT activationcode"+
                " FROM users"+
                " WHERE id=?";
        PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
        selectStmt.setInt(1, userId);
        ResultSet result = selectStmt.executeQuery();
        result.next();
        String activationCode = result.getString("activationcode");

        //Equals which stops any attacker from measuring the time taken to compare to figure out the activation code:
        boolean equals = true;
        for(int i=0;i<Math.min(enteredActivationCode.length(),activationCode.length());i++){
            if(enteredActivationCode.charAt(i)!=activationCode.charAt(i)){
                equals = false;
            }
        }
        if(!equals){
            return false;
        }

        String updateQuery =
                "UPDATE users"+
                " SET activated=1, activationcode = NULL" +
                " WHERE id=?";
        PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
        updateStmt.setInt(1,userId);
        updateStmt.executeUpdate();
        return true;

    }

    public boolean checkUser(String email, String password) throws SQLException {
        String query =
                "SELECT passwordhash" +
                " FROM users" +
                " WHERE email=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1,email);
        ResultSet result = stmt.executeQuery();
        result.next();
        String hashedPassword = result.getString("passwordhash");
        return BCrypt.checkpw(password,hashedPassword);
    }

    public void changeUserDetails(int userId, String newName, String newPassword) throws SQLException {
        if(newName!=null) {
            String query =
                    "UPDATE users" +
                    " SET name=?"+
                    " WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,newName);
            stmt.setInt(2,userId);
            stmt.executeUpdate();
        }
        if(newPassword!=null){
            String passwordHash = BCrypt.hashpw(newPassword,BCrypt.gensalt());
            String query =
                    "UPDATE users" +
                    " SET passwordhash=?"+
                    " WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,passwordHash);
            stmt.setInt(2,userId);
            stmt.executeUpdate();
        }
    }

    public User getUserInfo(int userid) throws SQLException {
        String query =
                "SELECT name,email" +
                " FROM users"+
                " WHERE id=?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1,userid);
        ResultSet result = stmt.executeQuery();
        result.next();
        return new User(result.getString("name"),result.getString("email"));
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
        stmt.setInt(7, course.getSchoolId());

        stmt.executeUpdate();
    }

    public List<Course> searchForCourses(String name, int schoolid, String teacherName, boolean online) throws SQLException {
        String query  ="SELECT * FROM courses WHERE" +
                        " (name LIKE ? or ? is null)" +
                        " AND (schoolid = ? or ?=-1)" +
                        " AND (id IN " +
                            " (SELECT courseid FROM teachesat WHERE teacherid IN " +
                                " (SELECT id FROM teachers WHERE name LIKE ?)) or ? is null)" +
                        " AND (online = ?)";
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
        String query = "SELECT * FROM courses WHERE id IN (SELECT courseid FROM savedcourse" +
                        " WHERE userid = ?)";

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

    public void removeBookmark(int course, int userId) throws SQLException {
        String query = "DELETE FROM savedcourse" +
                        " WHERE courseid = ? AND userid = ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, course);
        stmt.setInt(2, userId);
        stmt.executeUpdate();


    }

    //Review
    public void addReview(Review review) throws SQLException {
        String query = "INSERT INTO reviews(rating,time, text, userid, courseid, teacherid)" +
                        " VALUES (?,?,?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, review.getRating());
        stmt.setLong(2, review.getTime());
        stmt.setString(3, review.getText());
        stmt.setInt(4, review.getCourseid());
        stmt.setInt(5, review.getCourseid());
        stmt.setInt(6, review.getTeacherid());

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
            Review review  = new Review(results.getLong("time"),results.getInt("rating"), results.getString("text"), results.getInt("userid"), results.getInt("courseid"), results.getInt("teacherid"));
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
            Review review  = new Review(results.getLong("time"),results.getInt("rating"), results.getString("text"), results.getInt("userid"), results.getInt("courseid"), results.getInt("teacherid"));
            reviews.add(review);
        }
        return reviews;

    }


    //Teacher

    public void addTeacher(Teacher teacher, int courseid) throws SQLException {

        String query = "INSERT INTO teachers(name)" +
                " VALUES (?)";

        PreparedStatement stmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1,teacher.getName());
        stmt.executeUpdate();
        ResultSet result = stmt.getGeneratedKeys();
        result.next();
        int id = result.getInt(1);


        String query2 = "INSERT INTO teachesat(courseid, teacherid)" +
                " VALUES(?,?)";
        PreparedStatement stmt2 = conn.prepareStatement(query2);
        stmt2.setInt(1,courseid);
        stmt2.setInt(2,id);
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
        stmt.setString(1,startingString+'%');
        ResultSet results = stmt.executeQuery();

        List<Teacher> teachers = new ArrayList<Teacher>();
        while(results.next()){
            Teacher teacher  = new Teacher(results.getInt("id"),results.getString("name"));
            teachers.add(teacher);
        }

        return teachers;

    }

}
