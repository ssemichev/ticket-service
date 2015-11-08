package ticketbooking.service.base;

import java.util.List;

public interface FindBestMatch<Entity> {
    List<Entity> find(List<Entity> entities, int number, int minLevel, int maxLevel);
}
