package ticketbooking.model;

import java.util.Date;
import java.util.List;

public class Show {

    private int id;

    private Date date;

    private Venue venue;

    private List<SectionPrice> sectionPrices;

    public Show(int id, Date date, Venue venue, List<SectionPrice> sectionPrices) {
        this.id = id;
        this.date = date;
        this.venue = venue;
        this.sectionPrices = sectionPrices;
    }

    public int getId() {
        return id;
    }

    public Venue getVenue() {
        return venue;
    }

    public Date getDate() {
        return date;
    }

    public List<SectionPrice> getSectionPrices() {
        return sectionPrices;
    }
}
