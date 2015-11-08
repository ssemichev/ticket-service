package ticketbooking.service;

import org.junit.Before;
import org.junit.Test;
import ticketbooking.model.Show;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class BookingTicketServiceTest {

    private BookingTicketService ticketService;

    @Before
    public void setUp() throws Exception {
        Show show = new ResourceFactDatabaseAdapter("/facts.json").read().get(0);
        ticketService = new BookingTicketService(show);

    }

    @Test
    public void testNumSeatsAvailableGeneral() throws Exception {
        assertEquals(6250, ticketService.numSeatsAvailable(null));
        assertEquals(6250, ticketService.numSeatsAvailable(Optional.ofNullable(null)));
        assertEquals(1250, ticketService.numSeatsAvailable(Optional.of(1)));
        assertEquals(2000, ticketService.numSeatsAvailable(Optional.of(2)));
        assertEquals(0, ticketService.numSeatsAvailable(Optional.of(10)));
    }

    @Test
    public void testFindAndHoldSeats() throws Exception {
        ticketService.findAndHoldSeats(1, Optional.of(1), Optional.of(3), "testemail@gmail.com");
    }

    @Test
    public void testReserveSeats() throws Exception {

    }
}