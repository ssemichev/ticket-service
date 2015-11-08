package ticketbooking;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import ticketbooking.service.BookingTicketService;
import ticketbooking.service.CountingActor.Count;
import ticketbooking.service.CountingActor.CountBy;
import ticketbooking.service.CountingActor.Get;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static akka.pattern.Patterns.ask;
import static ticketbooking.common.spring.SpringExtension.SpringExtProvider;

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
            ActorRef counter = system.actorOf(SpringExtProvider.get(system).props("CountingActor"), "counter");

            // tell it to count three times
            counter.tell(new Count(), null);
            counter.tell(new Count(), null);
            counter.tell(new Count(), null);
            counter.tell(new CountBy(5), null);


            // print the result
            FiniteDuration duration = FiniteDuration.create(3, TimeUnit.SECONDS);
            Future<Object> result = ask(counter, new Get(), Timeout.durationToTimeout(duration));


            log.info("Got back " + Await.result(result, duration));
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
