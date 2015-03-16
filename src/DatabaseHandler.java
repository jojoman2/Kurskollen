import java.sql.Connection;
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

    public void addReview(int courseId, Review review){
    }

    public List<Review> getReviewsByCourse(int courseId, int teacherId){
        return null;
    }

    public List<Review> getReviewsByUser(int userId){
        return null;
    }


    //Teacher

    public void addTeacher(Teacher teacher){

    }

    public List<Teacher> getTeacherByCourse(int courseId){
        return null;
    }

    public List<Teacher> getTeachersByStartingString(String startingString){
        return null;
    }

}
