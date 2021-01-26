package com.king.graduation.consumer.Pojo;

import Enties.ClassSource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author king
 * @date 2021/1/26 - 下午1:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoadClassSourcePojo extends ClassSource {

    private List<ClassRemainder> remainder=new ArrayList<>();


}
