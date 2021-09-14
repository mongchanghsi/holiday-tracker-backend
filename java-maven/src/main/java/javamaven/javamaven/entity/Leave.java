package javamaven.javamaven.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Leave {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String date;
    @Transient
    private String day;

    public Leave (String date) {
        this.date = date;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "leaves")
    private List<User> users;

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
}
