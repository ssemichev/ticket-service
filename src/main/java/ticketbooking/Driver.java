package ticketbooking;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import ticketbooking.actor.MemoryFactDatabase;
import ticketbooking.actor.PerformanceBookingNode;
import ticketbooking.model.SeatHold;
import ticketbooking.model.Show;
import ticketbooking.model.request.performanceBooking.FindAndHoldSeatsRequest;
import ticketbooking.model.request.performanceBooking.NumSeatsAvailableRequest;
import ticketbooking.model.request.performanceBooking.ReserveSeatsRequest;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static akka.pattern.Patterns.ask;
import static ticketbooking.common.spring.SpringExtension.SpringExtProvider;

@SuppressWarnings("unchecked")
public class Driver {

    private static final Logger log = Logger.getLogger(Driver.class.getName());

    public static void main(String[] args) throws Exception {

        // create a spring context and scan the classes
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.scan("ticketbooking");
        ctx.refresh();

        // get hold of the actor system
        ActorSystem system = null;

        try {
            system = ctx.getBean(ActorSystem.class);

            log.info("Start");

            // use the Spring Extension to create props for a named actor bean
            ActorRef factDatabase = system.actorOf(SpringExtProvider.get(system).props("MemoryFactDatabase"), "factDatabase");
            factDatabase.tell(new MemoryFactDatabase.Initialize(), null);

            FiniteDuration duration = FiniteDuration.create(10, TimeUnit.SECONDS);
            Timeout timeout = Timeout.durationToTimeout(duration);

            Future<Object> result = ask(factDatabase, new MemoryFactDatabase.GetAll(), timeout);
            List<Show> shows = ((List<Show>)Await.result(result, duration));

            ActorRef performanceBookingNode = system.actorOf(Props.create(PerformanceBookingNode.class, factDatabase, shows.get(0).getId()));

            result = ask(performanceBookingNode, new NumSeatsAvailableRequest(Optional.of(1)), timeout);
            log.info("NumSeatsAvailable: " + Await.result(result, duration));

            result = ask(performanceBookingNode,
                    new FindAndHoldSeatsRequest(10, Optional.of(1), Optional.of(2), "testemail01@gmail.com"), timeout);
            SeatHold hold = (SeatHold)Await.result(result, duration);
            log.info("NumSeatsAvailable: " + hold.getBooking().getId());

            result = ask(performanceBookingNode, new NumSeatsAvailableRequest(Optional.of(1)), timeout);
            log.info("NumSeatsAvailable: " + Await.result(result, duration));

            result = ask(performanceBookingNode, new ReserveSeatsRequest(hold.getBooking().getId(), hold.getBooking().getCustomerEmail()), timeout);
            log.info("BookingId: " + Await.result(result, duration));

            result = ask(performanceBookingNode, new NumSeatsAvailableRequest(Optional.of(1)), timeout);
            log.info("NumSeatsAvailable: " + Await.result(result, duration));

            Await.result(result, duration);
            log.info("End");
        } catch (Exception e) {
            log.log( Level.SEVERE, "Failed getting result: " + e.getMessage());
            throw e;
        } finally {
            if (system != null) {
                system.terminate();
                Await.result(system.whenTerminated(), Duration.Inf());
            }
        }
    }
}
