package ticketbooking.actor;

import akka.actor.AbstractActor;
import org.springframework.context.annotation.Scope;

import javax.inject.Named;

@Named("BookingServiceNode")
@Scope("prototype")
public class BookingServiceNode extends AbstractActor {

}
