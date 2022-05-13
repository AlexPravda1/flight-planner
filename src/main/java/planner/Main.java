package planner;

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

        LeonApiDao leonApi = new LeonApiDaoImpl();
        System.out.println(leonApi.getAllByPeriod(volare, 10L));
        System.out.println(leonApi.getAllByPeriod(volare, 3L));
        System.out.println(leonApi.getAllActiveAircraft(volare));
        System.out.println(leonApi.getAllActiveAircraft(volare));
        System.out.println(leonApi.getAllActiveAircraft(volare));
    }
}
