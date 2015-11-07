package ticketbooking.model;

import ticketbooking.model.base.Capacity;

public class Seat implements Capacity {

    private int rowNumber;

    private int number;

    private int sectionId;

    public Seat(int rowNumber, int number, int sectionId) {
        this.rowNumber = rowNumber;
        this.number = number;
        this.sectionId = sectionId;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public int getCapacity() {
        return 1;
    }

    public int getSectionId() {
        return sectionId;
    }
}
