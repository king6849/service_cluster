package Enties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ClassSource {

  private long id;
  private String name;
  private java.sql.Date classStartTime;
  private java.sql.Date classEndTime;
  private String space;
  private double price;
  private long members;


}
