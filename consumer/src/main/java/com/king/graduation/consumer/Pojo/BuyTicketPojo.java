package com.king.graduation.consumer.Pojo;

/**
 * @author king
 * @date 2021/1/23 - 16:32
 */

public class BuyTicketPojo {

    private Adult adult;
    private Minor minor;

    public BuyTicketPojo() {
    }

    public BuyTicketPojo(Adult adult, Minor minor) {
        this.adult = adult;
        this.minor = minor;
    }

    public Adult getAdult() {
        return adult;
    }

    public void setAdult(Adult adult) {
        this.adult = adult;
    }

    public Minor getMinor() {
        return minor;
    }

    public void setMinor(Minor minor) {
        this.minor = minor;
    }


}
