package com.king.graduation.consumer.Pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author king
 * @date 2021/1/23 - 16:13
 */
@Getter
@Setter
public class Adult {

    private String ticketName;
    private int numbers;
    private double ticketPrice;
    private int pool;
    private String date;
    private long id;

    public Adult() {
    }

    public Adult(long id, String ticketName, int numbers, double price, int pool, String date) {
        this.id = id;
        this.ticketName = ticketName;
        this.numbers = numbers;
        this.ticketPrice = price;
        this.pool = pool;
        this.date = date;
    }
}
