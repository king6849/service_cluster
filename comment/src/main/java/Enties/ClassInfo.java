package Enties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClassInfo {

    private long id;
    private long iId;
    private long cId;
    private String name;
    private long remainderAm;
    private long remainderPm;

    public ClassInfo() {
    }

    public ClassInfo(long id, long iId, long cId, String name, long remainderAm, long remainderPm) {
        this.id = id;
        this.iId = iId;
        this.cId = cId;
        this.name = name;
        this.remainderAm = remainderAm;
        this.remainderPm = remainderPm;
    }
}
