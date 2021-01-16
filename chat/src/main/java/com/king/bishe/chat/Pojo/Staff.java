package com.king.bishe.chat.Pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Staff extends BaseInfo implements Serializable {

    private String nickName;

    public Staff(int id, String name, String avatar, String nickName) {
        super(id, name, 1);
        this.nickName = nickName;
    }


}
