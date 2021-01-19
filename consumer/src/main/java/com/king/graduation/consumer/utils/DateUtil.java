package com.king.graduation.consumer.utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author king
 * @date 2020/11/23
 */
public class DateUtil {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public static Date currentDate(int year, int month, int dates, int week) {
        Calendar calendar = Calendar.getInstance();
        //把日期往后增加一年.整数往后推,负数往前移动
        calendar.add(Calendar.YEAR, year);
        //把日期往后增加一个月.整数往后推,负数往前移动
        calendar.add(Calendar.DAY_OF_MONTH, month);
        //把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.DATE, dates);
        //把日期往后增加一个月.整数往后推,负数往前移动
        calendar.add(Calendar.WEEK_OF_MONTH, week);
        return calendar.getTime();
    }

    public static Date moreDate(int data) {
        return currentDate(0, 0, data, 0);
    }

    @Test
    public void test() {
        Calendar calendar = Calendar.getInstance();
        System.out.println(simpleDateFormat.format(calendar.getTime()));
        System.out.println(simpleDateFormat.format(DateUtil.currentDate(0, 0, 1, 0)));
    }

}
