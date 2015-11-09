package ticketbooking.actor;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import org.springframework.context.annotation.Scope;
import ticketbooking.model.Show;
import ticketbooking.common.Protocol;
import ticketbooking.service.ResourceFactDatabaseAdapter;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named("MemoryFactDatabase")
@Scope("prototype")
public class MemoryFactDatabase extends AbstractActor {
    final ResourceFactDatabaseAdapter factDatabaseAdapter;

    private List<Show> shows = new ArrayList<>();

    public static class Initialize implements Protocol {}

    public static class GetAll implements Protocol {}

    public static class GetById implements Protocol {
        public final int id;

        public GetById(int id) {
            this.id = id;
        }
    }

    @Inject
    public MemoryFactDatabase(@Named("ResourceFactDatabaseAdapter") ResourceFactDatabaseAdapter factDatabaseAdapter) {
        this.factDatabaseAdapter = factDatabaseAdapter;

        receive(ReceiveBuilder
                .match(Initialize.class, m -> this.shows = this.factDatabaseAdapter.read())
                .match(GetAll.class, m -> sender().tell(shows, self()))
                .match(GetById.class, m -> sender().tell(shows.stream().filter(s -> s.getId() == m.id).collect(Collectors.toList()), self()))
                .matchAny(this::unhandled)
                .build());
    }
}
