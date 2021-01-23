package com.king.graduation.consumer.Pojo;

import Enties.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author king
 * @date 2020/11/20
 */

@Getter
@Setter
@ToString
public class LoginUserPojo extends User {

    private String code;
    //余额
    private double overage;
    private String oldPassword;
}
