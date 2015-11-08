package ticketbooking.model;

public class EventSeat extends Seat {

    private boolean isOnHold;

    private String holdId;

    private boolean hasBooked;

    private String bookingId;

    public EventSeat(int rowNumber, int number, int sectionId) {
        super(rowNumber, number, sectionId);
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
        releaseHold();
    }

    public void removeBooking() {
        this.bookingId = "";
        this.hasBooked = false;
    }

    @Override
    public int getCapacity() {
        return (hasBooked() || isOnHold()) ? 0 : getCapacity();
    }
}
