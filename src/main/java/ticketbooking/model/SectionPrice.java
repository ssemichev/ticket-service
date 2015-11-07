package ticketbooking.model;

public class SectionPrice {

    private int id;

    private Section section;

    private float price;

    public SectionPrice(int id, Section section, float price) {
        this.id = id;
        this.section = section;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public Section getSection() {
        return section;
    }

    public float getPrice() {
        return price;
    }
}
