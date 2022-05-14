package planner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import planner.dao.LeonApiDao;
import planner.dao.impl.LeonApiDaoImpl;
import planner.model.Airline;

public class Main {
    public static void main(String[] args) {

        Airline volare = new Airline();
        volare.setName("Volare Aviation");
        volare.setLeonSubDomain("vlz");
        volare.setLeonApiKey(
                "ff0bd02c05f2d6916deeb8b9d022e03a388a7e0f48fb02e577faf41350d73cb32d47f6b0");

        Logger log = LogManager.getLogger();
        LeonApiDao leonApi = new LeonApiDaoImpl(log);
        log.info(leonApi.getAllActiveAircraft(volare));
        log.info(leonApi.getAllFlightsByPeriod(volare, 1L));
        log.info(leonApi.getAllFlightsByPeriodAndAircraftId(volare, 1L, 22249L));
        //log.info(leonApi.getAllFlightsByPeriod(volare, 2L));
        //log.info(leonApi.getAllFlightsByPeriod(volare, 3L));
    }
}
