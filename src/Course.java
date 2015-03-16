/**
 * Created by Fredrik on 2015-03-12.
 */
public class Course {

    private String courseCode;
    private String name;
    private String description;
    private float credits;
    private boolean online;
    private String link;

    public Course(String courseCode, String name, String description, float credits, boolean online, String link) {
        this.courseCode = courseCode;
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.online = online;
        this.link = link;
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

    public String getLink() {
        return link;
    }
}
