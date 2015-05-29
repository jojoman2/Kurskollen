package Beans;

/**
 * Created by Fredrik on 2015-03-12.
 */
public class Course {

    private int courseId;
    private String courseCode;
    private String name;
    private String description;
    private float credits;
    private boolean online;
    private int schoolId;

    public Course(String courseCode, String name, String description, float credits, boolean online, int schoolId) {
        this.courseCode = courseCode;
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.online = online;
        this.schoolId = schoolId;
    }

    public Course(int courseId,String courseCode, String name, String description, float credits, boolean online, int schoolId){
        this(courseCode, name, description ,credits ,online, schoolId);
        this.courseId = courseId;
    }

    public int courseId(){
        return courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getCredits() {
        return credits;
    }

    public boolean isOnline() {
        return online;
    }

    public int schoolId() {
        return schoolId;
    }


}
