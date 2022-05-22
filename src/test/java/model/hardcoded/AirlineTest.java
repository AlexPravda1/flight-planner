package model.hardcoded;

import planner.model.Airline;

public final class AirlineTest {
    public static Airline getAirlineWithIdAndLeonApiKey() {
        Airline airline = new Airline();
        airline.setId(1L);
        airline.setName("Volare Aviation");
        airline.setLeonSubDomain("vlz");
        airline.setLeonApiKey(
                "ff0bd02c05f2d6916deeb8b9d022e03a388a7e0f48fb02e577faf41350d73cb32d47f6b0");
        return airline;
    }
}
