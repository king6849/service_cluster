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

}
