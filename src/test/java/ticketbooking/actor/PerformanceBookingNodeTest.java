package ticketbooking.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import org.junit.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import ticketbooking.model.SeatHold;
import ticketbooking.model.Show;
import ticketbooking.model.request.performanceBooking.FindAndHoldSeatsRequest;
import ticketbooking.model.request.performanceBooking.NumSeatsAvailableRequest;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import static akka.pattern.Patterns.ask;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static ticketbooking.common.spring.SpringExtension.SpringExtProvider;

@SuppressWarnings("unchecked")
public class PerformanceBookingNodeTest {

    static AnnotationConfigApplicationContext ctx;
    static ActorSystem system;
    static ActorRef factDatabase;
    static FiniteDuration duration;
    static Timeout timeout;
    static List<Show> shows;

    @Test
    public void testNumSeatsAvailableRequest() throws Exception {
        ActorRef performanceBookingNode = system.actorOf(Props.create(PerformanceBookingNode.class, factDatabase, shows.get(0).getId()));

        Future<Object> result = ask(performanceBookingNode, new NumSeatsAvailableRequest(Optional.of(1)), timeout);
        assertEquals(1250, Await.result(result, duration));

        result = ask(performanceBookingNode, new NumSeatsAvailableRequest(Optional.ofNullable(null)), timeout);
        assertEquals(6250, Await.result(result, duration));
    }

    @Test
    public void testFindAndHoldSeatsRequest() throws Exception {
        ActorRef performanceBookingNode = system.actorOf(Props.create(PerformanceBookingNode.class, factDatabase, shows.get(0).getId()));

        Future<Object> result = ask(performanceBookingNode, new NumSeatsAvailableRequest(Optional.of(1)), timeout);
        assertEquals(1250, Await.result(result, duration));

        result = ask(performanceBookingNode, new FindAndHoldSeatsRequest(10, Optional.of(1), Optional.of(2), "testemail01@gmail.com"), timeout);
        SeatHold hold = (SeatHold)Await.result(result, duration);
        assertNotNull(hold);

        result = ask(performanceBookingNode, new NumSeatsAvailableRequest(Optional.of(1)), timeout);
        assertEquals(1240, Await.result(result, duration));
    }

    @Test
    public void testNotEnoughSeats() throws Exception {
        ActorRef performanceBookingNode = system.actorOf(Props.create(PerformanceBookingNode.class, factDatabase, shows.get(0).getId()));

        Future<Object> result = ask(performanceBookingNode, new NumSeatsAvailableRequest(Optional.of(1)), timeout);
        assertEquals(1250, Await.result(result, duration));

        result = ask(performanceBookingNode, new FindAndHoldSeatsRequest(1000, Optional.of(1), Optional.of(1), "testemail01@gmail.com"), timeout);
        SeatHold hold = (SeatHold)Await.result(result, duration);
        assertNotNull(hold);
        assertEquals(1000, hold.getBooking().getSeats().size());

        result = ask(performanceBookingNode, new NumSeatsAvailableRequest(Optional.of(1)), timeout);
        assertEquals(250, Await.result(result, duration));

        result = ask(performanceBookingNode, new FindAndHoldSeatsRequest(300, Optional.of(1), Optional.of(1), "testemail01@gmail.com"), timeout);
        SeatHold notEnoughSeatsHold = (SeatHold)Await.result(result, duration);
        assertNull(notEnoughSeatsHold.getBooking());
    }

    @BeforeClass
    public static void setUp() throws Exception {
        ctx = new AnnotationConfigApplicationContext();
        ctx.scan("ticketbooking");
        ctx.refresh();
        system = ctx.getBean(ActorSystem.class);

        factDatabase = system.actorOf(SpringExtProvider.get(system).props("MemoryFactDatabase"), "factDatabase");
        factDatabase.tell(new MemoryFactDatabase.Initialize(), null);

        duration = FiniteDuration.create(10, TimeUnit.SECONDS);
        timeout = Timeout.durationToTimeout(duration);

        Future<Object> result = ask(factDatabase, new MemoryFactDatabase.GetAll(), timeout);
        shows = ((List<Show>)Await.result(result, duration));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        system.terminate();
        Await.result(system.whenTerminated(), Duration.Inf());
    }
}