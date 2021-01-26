package com.king.graduation.consumer.Pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 一个封装对象，映射sql结果，插寻每种班级有多少个教练开设
 *
 * @Author king
 * @date 2021/1/26 - 下午12:35
 */
@Getter
@Setter
@ToString
public class ClassKinds {
    private long cId;
    private long numbers;
    private long members;

}
