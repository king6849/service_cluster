package Enties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author king
 */
@Getter
@Setter
@ToString
public class User implements Serializable {

    private long id;
    private String phone;
    private String nickName;
    private String password;
    private String avatar;
    private String sex;
    private long level;


}
