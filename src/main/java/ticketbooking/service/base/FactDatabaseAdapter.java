package ticketbooking.service.base;

import java.util.List;

public interface FactDatabaseAdapter<T> {
    void save(List<T> facts);
    List<T> read();
}
