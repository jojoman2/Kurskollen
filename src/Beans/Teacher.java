package beans;

/**
 * Created by Fredrik on 2015-03-12.
 */
public class Teacher {

    private int id;
    private String name;

    public Teacher(String name){
        this.name = name;
    }

    public Teacher(int id, String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId(){
        return id;
    }
}
