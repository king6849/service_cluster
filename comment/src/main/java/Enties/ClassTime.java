package Enties;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ClassTime {

  private long id;
  private String classLabel;
  @JsonFormat(pattern = "hh:MM")
  private java.sql.Time classStartTime;
  @JsonFormat(pattern = "hh:MM")
  private java.sql.Time classEndTime;



}
