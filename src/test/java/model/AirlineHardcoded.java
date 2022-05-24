package model;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.springframework.util.ResourceUtils.getFile;

import java.io.IOException;
import planner.model.Airline;

public final class AirlineHardcoded {

    public static Airline getAirlineWithIdAndLeonApiKey() {
        Airline airline = new Airline();
        airline.setId(1063L);
        airline.setName("Volare Aviation");
        airline.setLeonSubDomain("vlz");
        try {
            airline.setLeonApiKey(readFileToString(
                    getFile("classpath:leonApiKey/vlz.txt"), "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return airline;
    }
}
