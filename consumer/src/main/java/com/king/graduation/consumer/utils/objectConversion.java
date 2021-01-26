package com.king.graduation.consumer.utils;

import Enties.ClassSource;
import Enties.ClassTime;
import com.king.graduation.consumer.Pojo.ClassRemainder;
import com.king.graduation.consumer.Pojo.LoadClassSourcePojo;

import java.text.SimpleDateFormat;

/**
 * @author king
 * @date 2021/1/23 - 20:26
 */
public class objectConversion {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");

    public static LoadClassSourcePojo converToLoadClassSourcePojo(ClassSource classSource) {
        LoadClassSourcePojo loadClassSourcePojo = new LoadClassSourcePojo();
        loadClassSourcePojo.setId(classSource.getId());
        loadClassSourcePojo.setName(classSource.getName());
        loadClassSourcePojo.setClassStartTime(classSource.getClassStartTime());
        loadClassSourcePojo.setClassEndTime(classSource.getClassEndTime());
        loadClassSourcePojo.setSpace(classSource.getSpace());
        loadClassSourcePojo.setPrice(classSource.getPrice());
        loadClassSourcePojo.setMembers(classSource.getMembers());
        return loadClassSourcePojo;
    }

    public static ClassRemainder converToClassRemainder(ClassTime classTime) {
        ClassRemainder classRemainder = new ClassRemainder();
        classRemainder.setId(classTime.getId());
        classRemainder.setClassLabel(classTime.getClassLabel());
        classRemainder.setTime(simpleDateFormat.format(classTime.getClassStartTime()) + simpleDateFormat.format(classTime.getClassEndTime()));
        return classRemainder;
    }

}
