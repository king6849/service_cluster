package Enties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class TicketRecord {

    private long ticketRecordId;
    private int numbers;
    //下单时间
    private Date bookTime;
    private double totalPrice;
    //具体哪一天可以使用该门票进场
    private Date effectiveTime;
    private long uId;
    private long tId;

}
