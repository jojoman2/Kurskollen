import java.sql.Date;

/**
 * Created by Fredrik on 2015-03-12.
 */
public class Review {

    private Date time;
    private int rating;
    private String text;
    private int userid;

    public Review(Date time, int rating, String text, int userid) {
        this.time = time;
        this.rating = rating;
        this.text = text;
        this.userid = userid;
    }

    public Date getTime() {
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
}
