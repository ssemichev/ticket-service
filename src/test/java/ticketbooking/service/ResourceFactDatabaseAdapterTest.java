package ticketbooking.service;

import org.junit.Test;
import ticketbooking.model.Section;
import ticketbooking.model.SectionPrice;
import ticketbooking.model.Show;
import ticketbooking.model.Venue;

import java.util.*;

import static org.junit.Assert.*;

public class ResourceFactDatabaseAdapterTest {

    @Test
    public void testSave() throws Exception {
        List<Show> facts = new ArrayList<>();

        List<Section> sections = Arrays.asList(
                new Section(1, "Orchestra", "Orchestra", 25, 50),
                new Section(2, "Main", "Main", 20, 100),
                new Section(3, "Balcony1", "Balcony 1", 15, 100),
                new Section(4, "Balcony2", "Balcony 2", 15, 100)
        );

        List<SectionPrice> sectionsPrice = Arrays.asList(
                new SectionPrice(1, new Section(1, "Orchestra", "Orchestra", 25, 50), 100.0f),
                new SectionPrice(1, new Section(2, "Main", "Main", 20, 100), 75.0f),
                new SectionPrice(1, new Section(3, "Balcony1", "Balcony 1", 15, 100), 50.0f),
                new SectionPrice(1, new Section(4, "Balcony2", "Balcony 2", 15, 100), 40.0f)
        );

        facts.add(new Show(1, new Date(), new Venue(1, "The Wizard of Oz", "This new production of The Wizard of Oz is an enchanting adaptation of the all-time classic, totally reconceived for the stage", sections), sectionsPrice));
        facts.add(new Show(2, new Date(), new Venue(2, "Sherlock Holmes", "David Arquette stars in the title role as the legendary \"consulting detective,\" Sherlock Holmes", sections), sectionsPrice));
        facts.add(new Show(3, new Date(), new Venue(3, "Pericles", "Pericles, Prince of Tyre, sets sail on an extraordinary journey through the decades", sections), sectionsPrice));

        new ResourceFactDatabaseAdapter("/facts.json").save(facts);
    }

    @Test
    public void testRead() throws Exception {
        assertEquals(0, new ResourceFactDatabaseAdapter("/wrongPath.json").read().size());
        assertNotEquals(0, new ResourceFactDatabaseAdapter("/facts.json").read().size());
    }
}