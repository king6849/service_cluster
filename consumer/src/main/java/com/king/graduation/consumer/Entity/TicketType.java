package com.king.graduation.consumer.Entity;

import lombok.Data;

@Data
public class TicketType {

    private long ticketId;
    private String ticketName;
    private Double ticketPrice;
    private int allNumbers;
}

