package com.king.bishe.chat.Pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class BaseInfo implements Serializable {
    private int id;
    private String name;
    private String avatar;
    private int role;

    public BaseInfo(String avatar, int role) {
        this.avatar = avatar;
        this.role = role;
    }

    public BaseInfo(int id, String name, int role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
}
