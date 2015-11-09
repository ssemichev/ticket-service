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
        if (!(o instanceof Seat)) return false;

        Seat seat = (Seat) o;

        if (getRowNumber() != seat.getRowNumber()) return false;
        if (getNumber() != seat.getNumber()) return false;
        return getSectionId() == seat.getSectionId();

    }

    @Override
    public int hashCode() {
        int result = getRowNumber();
        result = 31 * result + getNumber();
        result = 31 * result + getSectionId();
        return result;
    }
}
