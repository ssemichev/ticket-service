package ticketbooking.model;


import java.util.Date;

public class Booking {

    private String id;

    private Show show;

    private String cancellationCode;

    private Date createdOn;

    private String customerEmail;

    public Booking(String id, Show show, String cancellationCode, Date createdOn, String customerEmail) {
        this.id = id;
        this.show = show;
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
}