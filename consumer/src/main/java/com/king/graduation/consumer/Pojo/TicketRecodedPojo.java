package com.king.graduation.consumer.Pojo;

import Enties.TicketRecord;
import Enties.TicketType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author king
 * @date 2020/12/1
 */
@Getter
@Setter
@ToString
public class TicketRecodedPojo extends TicketRecord {

    private long ticketId;
    private String ticketName;
    private double ticketPrice;
    private int allNumbers;


    private int discount;

    //用户电话
    private String phone;
    //昵称
    private String nickName;

    public void setTicketType(TicketType ticketType) {
        this.ticketName = ticketType.getTicketName();
        this.ticketPrice = ticketType.getTicketPrice();
    }


}
