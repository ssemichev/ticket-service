package ticketbooking.model.request.performanceBooking;

import ticketbooking.common.Protocol;

public class InitializeRequest implements Protocol {
    private int showId;

    public InitializeRequest(int showId) {
        this.showId = showId;
    }

    public int getShowId() {
        return showId;
    }
}
