package ticketbooking.model;

import java.io.Serializable;

public class Ticket implements Serializable {

    private int id;

    private float price;

    private Show show;

    private Seat seat;

    public Ticket(int id, float price, Show show, Seat seat) {
        this.id = id;
        this.price = price;
        this.show = show;
        this.seat = seat;
    }

    public int getId() {
        return id;
    }

    public float getPrice() {
        return price;
    }

    public Show getShow() {
        return show;
    }

    public Seat getSeat() {
        return seat;
    }
}
