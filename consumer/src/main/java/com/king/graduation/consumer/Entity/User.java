package com.king.graduation.consumer.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class User implements Serializable {

    private long id;
    private String openId;
    private String phone;
    private String sex;
    private String userAccount;
    private String nickName;
    private String password;
    private String avatar;
    private long level;


}
