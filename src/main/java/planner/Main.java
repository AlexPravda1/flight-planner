package planner;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import org.dozer.DozerBeanMapper;
import planner.dao.LeonApiDao;
import planner.dao.impl.LeonApiDaoImpl;
import planner.model.Airline;
import planner.service.LeonApiService;
import planner.service.impl.LeonApiServiceImpl;

@Log4j2
public class Main {
    public static void main(String[] args) throws IOException {

        Airline volare = new Airline();
        volare.setId(1L);
        volare.setName("Volare Aviation");
        volare.setLeonSubDomain("vlz");
        volare.setLeonApiKey(
                "ff0bd02c05f2d6916deeb8b9d022e03a388a7e0f48fb02e577faf41350d73cb32d47f6b0");

        LeonApiDao leonApiDao = new LeonApiDaoImpl();
        ObjectMapper objectMapper = new ObjectMapper();
        DozerBeanMapper mapper = new DozerBeanMapper();
        LeonApiService leonApiService = new LeonApiServiceImpl(leonApiDao);
        String allActiveAircraftJson = leonApiService.getAllActiveAircraft(volare);
        log.info(allActiveAircraftJson);
        log.info(leonApiService.getAllFlightsByPeriod(volare, 1L));

        // 2-SWIS
        log.info(leonApiService.getAllFlightsByPeriodAndAircraftId(volare, 2L, 22249L));

        // 2-OOOX
        log.info(leonApiService.getAllFlightsByPeriodAndAircraftId(volare, 0, 17038L));
        //log.info(leonApi.getAllFlightsByPeriod(volare, 2L));
        //log.info(leonApi.getAllFlightsByPeriod(volare, 3L));

        /*
        Root root = objectMapper.readValue(allActiveAircraftJson, Root.class);
        System.out.println(root);

        BeanMappingBuilder aircraftMapBuilder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(AircraftList.class, Aircraft.class)
                        .fields("aircraftNid", "id")
                        .fields("acftType.isAircraft", "isAircraft")
                        .fields("acftType.icao", "type");
            }
        };

        mapper.addMapping(aircraftMapBuilder);

        Aircraft aircraft = mapper.map(root.getData().getAircraftList().get(7), Aircraft.class);
        System.out.println(aircraft.toString());*/
    }
}
