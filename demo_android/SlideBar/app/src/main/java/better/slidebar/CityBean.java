package better.slidebar;

/**
 * Created by better on 16/9/10.
 */
public class CityBean {
    private String city;
    private String letter;
    private String head;

    public CityBean(String city, String letter, String head) {
        this.city = city;
        this.letter = letter;
        this.head = head;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
