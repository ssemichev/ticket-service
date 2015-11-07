package ticketbooking.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;
import scala.concurrent.duration.Duration;
import ticketbooking.model.Show;

import java.util.List;
import java.util.concurrent.TimeUnit;
import static akka.pattern.Patterns.ask;
import static org.junit.Assert.assertEquals;
import static ticketbooking.common.spring.SpringExtension.SpringExtProvider;

@SuppressWarnings("unchecked")
public class MemoryFactDatabaseTest {

    AnnotationConfigApplicationContext ctx = null;
    ActorSystem system = null;

    @Test
    public void testGetAllWithoutInitCommand() throws Exception {
        ActorRef factDatabase = system.actorOf(SpringExtProvider.get(system).props("MemoryFactDatabase"), "factDatabase");

        FiniteDuration duration = FiniteDuration.create(3, TimeUnit.SECONDS);
        Future<Object> result = ask(factDatabase, new MemoryFactDatabase.GetAll(), Timeout.durationToTimeout(duration));

        assertEquals(0, ((List<Show>)Await.result(result, duration)).size());
    }

    @Test
    public void testGetAll() throws Exception {
        ActorRef factDatabase = system.actorOf(SpringExtProvider.get(system).props("MemoryFactDatabase"), "factDatabase");

        factDatabase.tell(new MemoryFactDatabase.Initialize(), null);

        FiniteDuration duration = FiniteDuration.create(3, TimeUnit.SECONDS);
        Future<Object> result = ask(factDatabase, new MemoryFactDatabase.GetAll(), Timeout.durationToTimeout(duration));

        assertEquals(3, ((List<Show>)Await.result(result, duration)).size());
    }

    @Test
    public void testGetById() throws Exception {
        ActorRef factDatabase = system.actorOf(SpringExtProvider.get(system).props("MemoryFactDatabase"), "factDatabase");

        factDatabase.tell(new MemoryFactDatabase.Initialize(), null);

        int showId = 1;

        FiniteDuration duration = FiniteDuration.create(3, TimeUnit.SECONDS);
        Future<Object> result = ask(factDatabase, new MemoryFactDatabase.GetById(showId), Timeout.durationToTimeout(duration));

        List<Show> expected = ((List<Show>)Await.result(result, duration));

        assertEquals(1, expected.size());
        assertEquals(showId, expected.get(0).getId());
    }

    @Before
    public void setUp() throws Exception {
        ctx = new AnnotationConfigApplicationContext();
        ctx.scan("ticketbooking");
        ctx.refresh();

        system = ctx.getBean(ActorSystem.class);
    }

    @After
    public void tearDown() throws Exception {
        system.terminate();
        Await.result(system.whenTerminated(), Duration.Inf());
    }
}