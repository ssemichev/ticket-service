package ticketbooking.service.base;

import org.springframework.context.annotation.Scope;

import javax.inject.Named;
import java.util.List;

@Named("FactDatabaseAdapter")
@Scope("prototype")
public interface FactDatabaseAdapter<T> {
    void save(List<T> facts);
    List<T> read();
}
