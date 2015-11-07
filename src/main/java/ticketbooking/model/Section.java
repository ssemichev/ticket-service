package ticketbooking.model;

import ticketbooking.model.base.Capacity;

public class Section implements Capacity {

    private int id;

    private String name;

    private String description;

    private int numberOfRows;

    private int rowCapacity;

    public Section(int id, String name, String description, int numberOfRows, int rowCapacity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberOfRows = numberOfRows;
        this.rowCapacity = rowCapacity;
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

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getRowCapacity() {
        return rowCapacity;
    }

    @Override
    public int getCapacity() {
        return numberOfRows * rowCapacity;
    }
}
