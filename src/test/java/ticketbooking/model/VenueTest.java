package ticketbooking.model;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class VenueTest {

    @Test
    public void testGetCapacity() throws Exception {
        List<Section> sections = Arrays.asList(
                new Section(1, "s1", "s1", 10, 10),
                new Section(1, "s1", "s1", 2, 10));

        Venue venue = new Venue(1, "Venue1", "Venue1", sections);

        int result = venue.getCapacity();
        assertEquals(120, result);
    }
}