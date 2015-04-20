package Beans;

/**
 * Created by Fredrik on 2015-03-12.
 */
public class Review {

    private long time;
    private int rating;
    private String text;
    private String useremail;
    private int courseid;
    private int teacherid;

    public Review(long time, int rating, String text, String useremail, int courseid, int teacherid) {
        this.time = time;
        this.rating = rating;
        this.text = text;
        this.useremail = useremail;
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

    public String userEmail(){
        return useremail;
    }

    public int courseid() {
        return courseid;
    }

    public int teacherid() {
        return teacherid;
    }

}
