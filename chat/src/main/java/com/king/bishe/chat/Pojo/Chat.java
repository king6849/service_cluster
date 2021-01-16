package com.king.bishe.chat.Pojo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Chat {

    private long id;
    private String fromWho;
    private String target;
    private String msgType;
    private Date makeTime;
    private String msg;
}
