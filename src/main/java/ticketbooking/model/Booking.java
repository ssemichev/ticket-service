package ticketbooking.model;


import java.util.Date;
import java.util.List;

public class Booking {

    private String id;

    private Show show;

    private List<Seat> seats;

    private String cancellationCode;

    private Date createdOn;

    private String customerEmail;

    public Booking(String id, Show show, List<Seat> seats, String cancellationCode, Date createdOn, String customerEmail) {
        this.id = id;
        this.show = show;
        this.seats = seats;
        this.cancellationCode = cancellationCode;
        this.createdOn = createdOn;
        this.customerEmail = customerEmail;
    }

    public String getId() {
        return id;
    }

    public Show getShow() {
        return show;
    }

    public String getCancellationCode() {
        return cancellationCode;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public List<Seat> getSeats() {
        return seats;
    }
}