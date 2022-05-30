package planner.util;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.springframework.util.ResourceUtils.getFile;

import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import planner.model.Airline;

@Log4j2
public final class AirlineUtil {
    public static Airline getVlzAirline() {
        Airline airline = new Airline();
        airline.setId(1063L);
        airline.setName("Volare Aviation UK");
        airline.setLeonSubDomain("vlz");
        try {
            airline.setLeonApiKey(readFileToString(
                    getFile("classpath:leonApiKey/vlz.txt"), "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.debug(airline.getName() + " airline provided from AirlineUtil");
        return airline;
    }
}
