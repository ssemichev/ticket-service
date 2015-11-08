package ticketbooking.service;

import org.springframework.context.annotation.Scope;
import ticketbooking.model.*;
import ticketbooking.service.base.TicketService;
import javax.inject.Named;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Named("BookingTicketService")
@Scope("prototype")
public class BookingTicketService implements TicketService {

    private FindTicketsService findTicketsService;

    // Hold duration in seconds
    private int holdDuration;

    private int minLevel, maxLevel;
    private Show show;
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

        List<EventSeat> seatsToOnHold = findTicketsService.find(availableSeats, numSeats, min, max);

        Date date = new Date();
        Format formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String bookingId = show.getId() + "-" + customerEmail + "-" + formatter.format(new Date()) + "-" + min + "-" + max + "-" + numSeats;

        availableSeats.stream().filter(seatsToOnHold::contains).forEach(s -> s.setOnHold(bookingId));

        Booking booking = new Booking(bookingId, show,
                seatsToOnHold.stream().map(es -> new Seat(es.getRowNumber(), es.getNumber(), es.getSectionId())).collect(Collectors.toList()),
                bookingId, date, customerEmail);

        holds.add(booking);

        return new SeatHold(booking, holdDuration);
    }

    @Override
    public String reserveSeats(String bookingId, String customerEmail) {
        List<Booking> bookings = holds.stream().filter(h -> Objects.equals(h.getId(), bookingId)).limit(1).collect(Collectors.toList());

        if (bookings.size() != 1) {
            return null;
        }

        Booking booking = bookings.get(0);

        booking.getSeats().forEach(
                s -> seats.get(s.getSectionId()).stream()
                        .filter(es -> es.getNumber() == s.getNumber() && es.getRowNumber() == s.getRowNumber())
                        .forEach(seat -> seat.setBooking(booking.getId()))
        );

        return booking.getId();
    }

    private Map<Integer, List<EventSeat>> populateEventSeats(List<Section> sections) {
        Map<Integer, List<EventSeat>> seats = new HashMap<>();

        for (Section section : sections) {
            List<EventSeat> eventSeats = new ArrayList<>(section.getCapacity());

            for (int r = 1; r <= section.getNumberOfRows(); r++)
                for (int n = 1; n <= section.getRowCapacity(); n++)
                    eventSeats.add(new EventSeat(r, n, section.getId()));

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
        Date current = new Date();

        List<Booking> expiredHolds = holds.stream()
                .filter(h -> (current.getTime() - h.getCreatedOn().getTime())  > holdDuration * 1000)
                .collect(Collectors.toList());

        //release expired holds from seats
        expiredHolds.forEach(h -> h.getSeats().forEach(
                s -> seats.get(s.getSectionId()).stream()
                        .filter(es -> es.getNumber() == s.getNumber() && es.getRowNumber() == s.getRowNumber())
                        .forEach(EventSeat::releaseHold)
        ));

        //remove expired holds
        expiredHolds.forEach(holds::remove);
    }

    private void Initialize(Show show, int holdDuration) {
        this.show = show;
        List<Section> sections = show.getVenue().getSections();
        minLevel = sections.stream().map(Section::getId).min(Integer::compare).get();
        maxLevel = sections.stream().map(Section::getId).max(Integer::compare).get();
        this.seats = populateEventSeats(sections);
        this.holdDuration = holdDuration;
        this.findTicketsService = new FindTicketsService();
    }
}
