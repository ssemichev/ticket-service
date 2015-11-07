package ticketbooking.service;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import ticketbooking.model.Show;
import ticketbooking.service.base.FactDatabaseAdapter;
import javax.inject.Named;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Named("ResourceFactDatabaseAdapter")
@Scope("prototype")
public class ResourceFactDatabaseAdapter implements FactDatabaseAdapter<Show> {
    private static final Logger log = Logger.getLogger(ResourceFactDatabaseAdapter.class.getName());

    private String factPath;

    @Autowired
    public ResourceFactDatabaseAdapter(@Value("${facts.path:/facts.json}") String factPath) {
        System.out.println("ResourceFactDatabaseAdapter");
        this.factPath = factPath;
    }

    @Override
    public void save(List<Show> facts) {
        JsonWriter.objectToJson(facts);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Show> read() {
        List<Show> result;

        try {
            String json = new String(Files.readAllBytes(Paths.get(ResourceFactDatabaseAdapter.class.getResource(factPath).toURI())));
            result = (ArrayList<Show>)JsonReader.jsonToJava(json);
        }
        catch (Exception ex) {
            log.warning("Cannot load facts database. " + ex.getMessage());
            result = new ArrayList<>();
        }

        return result;
    }
}
