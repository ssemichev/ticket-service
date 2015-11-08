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

    public int getSectionId() {
        return sectionId;
    }

    @Override
    public int getCapacity() {
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return seat.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + sectionId;
        hash = hash * 31 + rowNumber;
        hash = hash * 13 + number;
        return hash;
    }
}
