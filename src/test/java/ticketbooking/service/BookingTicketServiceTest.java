package ticketbooking.service;

import org.junit.Before;
import org.junit.Test;
import ticketbooking.model.Show;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        assertEquals(1250, ticketService.numSeatsAvailable(Optional.of(1)));
        ticketService.findAndHoldSeats(1, Optional.of(1), Optional.of(3), "testemail01@gmail.com");
        Thread.sleep(500);
        assertEquals(1249, ticketService.numSeatsAvailable(Optional.of(1)));
        ticketService.findAndHoldSeats(10, Optional.of(1), Optional.of(3), "testemail02@gmail.com");
        assertEquals(1239, ticketService.numSeatsAvailable(Optional.of(1)));
        Thread.sleep(700);
        assertEquals(1240, ticketService.numSeatsAvailable(Optional.of(1)));
        Thread.sleep(500);
        assertEquals(1250, ticketService.numSeatsAvailable(Optional.of(1)));
    }

    @Test
    public void testReserveSeats() throws Exception {
        Format formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String bookingId = 1 + "-" + "customerEmail" + "-" + formatter.format(new Date()) + "-";
    }
}