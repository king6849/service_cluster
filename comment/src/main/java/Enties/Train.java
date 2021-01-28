package Enties;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Train implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private long id;
    private java.sql.Date makeTime;
    private long members;
    private long cId;
    private long iId;
    private long uTime;
    private int status;

}
