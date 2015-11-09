package ticketbooking.model.request.performanceBooking;

import ticketbooking.common.Protocol;

public class ReserveSeatsRequest implements Protocol {

    private String bookingId;

    private String customerEmail;

    public ReserveSeatsRequest(String bookingId, String customerEmail) {
        this.bookingId = bookingId;
        this.customerEmail = customerEmail;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}
