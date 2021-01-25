package Enties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class Train {

    private long id;
    private java.sql.Date makeTime;
    private String members;
    private long cId;
    private long iId;
    private long uTime;

    public Train() {
    }

    public Train(long id, Date makeTime, String members, long cId, long iId) {
        this.id = id;
        this.makeTime = makeTime;
        this.members = members;
        this.cId = cId;
        this.iId = iId;
    }
}
