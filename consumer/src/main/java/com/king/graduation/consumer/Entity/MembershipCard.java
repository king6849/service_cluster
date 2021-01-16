package com.king.graduation.consumer.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class MembershipCard {

    private Integer id;

    /**
     * 余额
     */
    private Double overage;


    private Date makeTime;

    /**
     * 积分
     */
    private Integer integral;


}

