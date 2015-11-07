package ticketbooking.model;

import ticketbooking.common.Protocol;
import java.util.Optional;

public class NumSeatsAvailableRequest implements Protocol {
    private Optional<Integer> venueLevel;

    public NumSeatsAvailableRequest() { }

    public NumSeatsAvailableRequest(Optional<Integer> venueLevel) {
        this.venueLevel = venueLevel;
    }

    public Optional<Integer> getVenueLevel() {
        return venueLevel;
    }
}
