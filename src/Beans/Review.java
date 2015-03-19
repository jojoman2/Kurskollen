package Beans;

/**
 * Created by Fredrik on 2015-03-12.
 */
public class Review {

    private long time;
    private int rating;
    private String text;
    private int userid;
    private int courseid;
    private int teacherid;

    public Review(long time, int rating, String text, int userid, int courseid, int teacherid) {
        this.time = time;
        this.rating = rating;
        this.text = text;
        this.userid = userid;
        this.courseid = courseid;
        this.teacherid = teacherid;
    }

    public long getTime() {
        return time;
    }

    public int getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public int getUserid(){
        return userid;
    }

    public int getCourseid() {
        return courseid;
    }

    public int getTeacherid() {
        return teacherid;
    }

}
