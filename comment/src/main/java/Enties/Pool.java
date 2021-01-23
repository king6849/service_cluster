package Enties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Pool {

    private long id;
    private String poolName;
    private int totalTicket;

    public Pool() {
    }

    public Pool(long id, String poolName, int totalTicket) {
        this.id = id;
        this.poolName = poolName;
        this.totalTicket = totalTicket;
    }
}
