package ticketbooking.model;

import ticketbooking.model.base.Capacity;

import java.util.List;

public class Venue implements Capacity {

    private int id;

    private String name;

    private String description;

    private List<Section> sections;

    public Venue(int id, String name, String description, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sections = sections;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public int getCapacity() {
        return sections.stream().map(Section::getCapacity).reduce(0, Integer::sum);
    }
}
