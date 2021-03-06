package Enties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;


/**
 * @author king
 */
@Getter
@Setter
@ToString
public class TicketRecord {

    private long id;
    private String orderNumber;
    private int numbers;
    private double money;
    private Date bookTime;
    private Date effectiveTime;
    private long uId;
    private long tId;

    public TicketRecord(long id, String orderNumber, int numbers, double money, Date bookTime, Date effectiveTime, long uId, long tId) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.numbers = numbers;
        this.money = money;
        this.bookTime = bookTime;
        this.effectiveTime = effectiveTime;
        this.uId = uId;
        this.tId = tId;
    }

    public TicketRecord() {
    }
}
