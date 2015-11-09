package ticketbooking.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import akka.util.Timeout;
import org.springframework.context.annotation.Scope;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;
import ticketbooking.model.Show;
import ticketbooking.model.request.performanceBooking.FindAndHoldSeatsRequest;
import ticketbooking.model.request.performanceBooking.NumSeatsAvailableRequest;
import ticketbooking.model.request.performanceBooking.ReserveSeatsRequest;
import ticketbooking.service.BookingTicketService;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

@Named("PerformanceBookingNode")
@Scope("prototype")
@SuppressWarnings("unchecked")
public class PerformanceBookingNode extends AbstractActor {

    private ActorRef factDatabase;

    final BookingTicketService bookingTicketService;

    @Inject
    public PerformanceBookingNode(ActorRef factDatabase, int showId) throws Exception {
        this.factDatabase = factDatabase;

        FiniteDuration duration = FiniteDuration.create(5, TimeUnit.SECONDS);
        Future<Object> result = ask(this.factDatabase, new MemoryFactDatabase.GetById(showId), Timeout.durationToTimeout(duration));
        List<Show> shows = ((List<Show>)Await.result(result, duration));

        this.bookingTicketService = new BookingTicketService(shows.get(0));

        receive(ReceiveBuilder
                .match(NumSeatsAvailableRequest.class,
                        r -> sender().tell(bookingTicketService
                                .numSeatsAvailable(r.getVenueLevel()), self()))
                .match(FindAndHoldSeatsRequest.class,
                        r -> sender().tell(bookingTicketService
                                .findAndHoldSeats(r.getNumSeats(), r.getMinLevel(), r.getMaxLevel(), r.getCustomerEmail()), self()))
                .match(ReserveSeatsRequest.class,
                        r -> sender().tell(bookingTicketService.reserveSeats(r.getBookingId(), r.getCustomerEmail()), self()))
                .matchAny(this::unhandled)
                .build());
    }
}
