package Enties;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ClassTime {

    private long id;
    private java.sql.Time startTime;
    private java.sql.Time endTime;

}
