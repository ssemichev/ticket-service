package ticketbooking.model.request.performanceBooking;

import ticketbooking.common.Protocol;

import java.util.Optional;

public class FindAndHoldSeatsRequest implements Protocol {

    private int numSeats;

    private Optional<Integer> minLevel;

    private Optional<Integer> maxLevel;

    private String customerEmail;

    public FindAndHoldSeatsRequest(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail) {
        this.numSeats = numSeats;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.customerEmail = customerEmail;
    }

    public int getNumSeats() {
        return numSeats;
    }

    public Optional<Integer> getMinLevel() {
        return minLevel;
    }

    public Optional<Integer> getMaxLevel() {
        return maxLevel;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}
