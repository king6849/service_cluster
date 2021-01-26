package Enties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

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

  public ClassSource() {
  }

  public ClassSource(long id, String name, Date classStartTime, Date classEndTime, String space, double price, long members) {
    this.id = id;
    this.name = name;
    this.classStartTime = classStartTime;
    this.classEndTime = classEndTime;
    this.space = space;
    this.price = price;
    this.members = members;
  }
}
