package ticketbooking.model;

public class TicketRequest {

    private int ticketPrice;

    private int quantity;

    public TicketRequest(int ticketPrice, int quantity) {
        this.ticketPrice = ticketPrice;
        this.quantity = quantity;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}
