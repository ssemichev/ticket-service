package ticketbooking.model;

import ticketbooking.model.base.Capacity;

public class EventSeat implements Capacity {
    private Seat seat;

    private boolean isOnHold;

    private String holdId;

    private boolean hasBooked;

    private String bookingId;

    public EventSeat(Seat seat) {
        this.seat = seat;
    }

    public Seat getSeat() {
        return seat;
    }

    public boolean isOnHold() {
        return isOnHold;
    }

    public String getHoldId() {
        return holdId;
    }

    public void setOnHold(String holdId) {
        this.holdId = holdId;
        this.isOnHold = true;
    }

    public void releaseHold() {
        this.holdId = "";
        this.isOnHold = false;
    }

    public boolean hasBooked() {
        return hasBooked;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBooking(String bookingId) {
        this.bookingId = bookingId;
        this.hasBooked = true;
    }

    public void removeBooking() {
        this.bookingId = "";
        this.hasBooked = false;
    }

    @Override
    public int getCapacity() {
        return (hasBooked() || isOnHold()) ? 0 : seat.getCapacity();
    }
}
