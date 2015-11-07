package ticketbooking.service;

import ticketbooking.model.SeatHold;
import ticketbooking.service.base.TicketService;
import javax.inject.Named;
import java.util.Optional;

@Named("BookingService")
public class BookingService implements TicketService {
    @Override
    public int numSeatsAvailable(Optional<Integer> venueLevel) {
        Integer _venueLevel = venueLevel.orElse(new Integer(0));
        return _venueLevel;
    }

    @Override
    public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail) {
        return null;
    }

    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {
        return null;
    }
}
