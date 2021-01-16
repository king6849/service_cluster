package com.king.bishe.chat.Pojo;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString
public class User extends BaseInfo implements java.io.Serializable {

    private String nickName;


    public User(String nickName,String avatar) {
        super(avatar,0);
        this.nickName = nickName;
    }
}