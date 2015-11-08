package ticketbooking.service;

import ticketbooking.model.EventSeat;
import ticketbooking.model.Seat;
import ticketbooking.service.base.FindBestMatch;

import javax.inject.Named;
import java.util.List;

@Named("FindTicketsService")
public class FindTicketsService implements FindBestMatch<EventSeat> {

    @Override
    public List<EventSeat> find(List<EventSeat> seats, int number, int minLevel, int maxLevel) {
        if (seats.size() < number) return null;

        return seats.subList(0, number);
    }

}
