package ticketbooking.model;

public class SeatHold {

    private Booking booking;

    private int holdDuration;

    public SeatHold(Booking booking, int holdDuration) {
        this.booking = booking;
        this.holdDuration = holdDuration;
    }

    public Booking getBooking() {
        return booking;
    }

    public int getHoldDuration() {
        return holdDuration;
    }
}
