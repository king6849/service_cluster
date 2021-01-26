package com.king.graduation.consumer.Pojo;

import Enties.ClassTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author king
 * @date 2021/1/26 - 下午7:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ClassRemainder extends ClassTime {
    private String time;
    private long remainder;

}
