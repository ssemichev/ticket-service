package ticketbooking.service;

import akka.actor.UntypedActor;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

/**
 * An actor that can count using an injected CountingService.
 *
 * @note The scope here is prototype since we want to create a new actor
 * instance for use of this bean.
 */
@Named("CountingActor")
@Scope("prototype")
public class CountingActor extends AbstractActor {

    public static class Count {}

    public static class Get {}

    public static class CountBy {
        public final int inc;

        public CountBy(int inc) {
            this.inc = inc;
        }
    }

    // the service that will be automatically injected
    final CountingService countingService;

    @Inject
    public CountingActor(@Named("CountingService") CountingService countingService) {
        this.countingService = countingService;

        receive(ReceiveBuilder
                .match(Count.class, m -> count = countingService.increment(count))
                .match(CountBy.class, m -> count = countingService.incrementBy(count, m.inc))
                .match(Get.class, m -> sender().tell(count, self()))
                .matchAny(this::unhandled)
        .build());
    }

    private int count = 0;

//  @Override
//  public void onReceive(Object message) throws Exception {
//    if (message instanceof Count) {
//      count = countingService.increment(count);
//    } else if (message instanceof Get) {
//      getSender().tell(count, getSelf());
//    } else {
//      unhandled(message);
//    }
//  }


}
