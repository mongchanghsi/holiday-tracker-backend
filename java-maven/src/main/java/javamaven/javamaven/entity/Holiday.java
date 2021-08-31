package javamaven.javamaven.entity;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "holiday")
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private String date;

    @Transient
    private String day;

    public Holiday() {
        this.name = "";
        this.date = "";
    }

    public Holiday(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date _date = df.parse(this.date);
            DateFormat df2 = new SimpleDateFormat("EEEE");
            return df2.format(_date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return "Unable to parse date";
        }
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", day='" + day + '\'' +
                '}';
    }
}
