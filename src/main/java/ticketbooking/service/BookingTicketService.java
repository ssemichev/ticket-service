package ticketbooking.service;

import ticketbooking.model.*;
import ticketbooking.service.base.TicketService;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@Named("BookingTicketService")
public class BookingTicketService implements TicketService {

    private FindTicketsService findTicketsService;

    // Hold duration in seconds
    private int holdDuration;

    private int minLevel, maxLevel;

    private Map<Integer, List<EventSeat>> seats;
    private List<Booking> holds = new LinkedList<>();

    public BookingTicketService(Show show, int holdDuration) {
        Initialize(show, holdDuration);
    }

    public BookingTicketService() {}

    public BookingTicketService(Show show) {
        this(show, 1);
    }

    @Override
    public int numSeatsAvailable(Optional<Integer> venueLevel) {
        cleanUpHolds();

        return (venueLevel != null && venueLevel.isPresent()) ? getAvailableSeats(venueLevel.get(), venueLevel.get()).size()
                : getAvailableSeats(minLevel, maxLevel).size();
    }

    @Override
    public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail) {
        cleanUpHolds();

        int min = minLevel.orElse(this.minLevel);
        int max = minLevel.orElse(this.maxLevel);

        List<EventSeat> availableSeats = getAvailableSeats(min, max);

        if (availableSeats.size() < numSeats) return new SeatHold(null, holdDuration);

        findTicketsService.find(availableSeats, numSeats, min, max);

        return null;
    }

    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {
        cleanUpHolds();

        return null;
    }

    private Map<Integer, List<EventSeat>> populateEventSeats(List<Section> sections) {
        Map<Integer, List<EventSeat>> seats = new HashMap<>();

        for (Section section : sections) {
            List<EventSeat> eventSeats = new ArrayList<>(section.getCapacity());

            for (int r = 1; r <= section.getNumberOfRows(); r++)
                for (int n = 1; n <= section.getRowCapacity(); n++)
                    eventSeats.add(new EventSeat(new Seat(r, n, section.getId())));

            seats.put(section.getId(), eventSeats);
        }

        return seats;
    }

    private List<EventSeat> getAvailableSeats(int minLevel, int maxLevel) {
        return seats.entrySet().stream()
                .filter(s -> s.getKey() >= minLevel && s.getKey() <= maxLevel).flatMap(s -> s.getValue().stream())
                .filter(s -> !(s.isOnHold() || s.hasBooked())).collect(Collectors.toList());
    }

    private void cleanUpHolds() {
//        long seconds = (d2.getTime()-d1.getTime())/1000;
    }

    private void Initialize(Show show, int holdDuration) {
        List<Section> sections = show.getVenue().getSections();
        minLevel = sections.stream().map(Section::getId).min(Integer::compare).get();
        maxLevel = sections.stream().map(Section::getId).max(Integer::compare).get();
        this.seats = populateEventSeats(sections);
        this.holdDuration = holdDuration;
        this.findTicketsService = new FindTicketsService();
    }
}
