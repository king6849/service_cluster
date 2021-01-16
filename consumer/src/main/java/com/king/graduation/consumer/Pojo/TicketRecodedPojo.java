package com.king.graduation.consumer.Pojo;

import com.king.graduation.consumer.Entity.TicketRecord;
import com.king.graduation.consumer.Entity.TicketType;
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
    //折扣
    private int discount;

    public void setTicketType(TicketType ticketType) {
        this.ticketId = ticketType.getTicketId();
        this.ticketName = ticketType.getTicketName();
        this.ticketPrice = ticketType.getTicketPrice();
        this.allNumbers = ticketType.getAllNumbers();
    }

    //用户电话
    private String phone;
    //昵称
    private String nickName;
}
