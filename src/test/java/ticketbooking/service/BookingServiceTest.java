package ticketbooking.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Optional;
import static org.junit.Assert.assertEquals;

public class BookingServiceTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testNumSeatsAvailable() throws Exception {
        int result = new BookingService().numSeatsAvailable(Optional.of(1));
        assertEquals(1, result);
    }

    @Test
    public void testFindAndHoldSeats() throws Exception {

    }

    @Test
    public void testReserveSeats() throws Exception {

    }
}