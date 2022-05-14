package planner.util;

import static planner.model.leon.LeonQueryTemplate.AIRCRAFT;
import static planner.model.leon.LeonQueryTemplate.CREW;
import static planner.model.leon.LeonQueryTemplate.END_AIRPORT;
import static planner.model.leon.LeonQueryTemplate.ERROR;
import static planner.model.leon.LeonQueryTemplate.FILTER_ALL_ACTIVE_AIRCRAFT;
import static planner.model.leon.LeonQueryTemplate.FILTER_CONDITIONS_FLIGHT_LIST_ALL_AIRCRAFT;
import static planner.model.leon.LeonQueryTemplate.FILTER_FLIGHT_LIST;
import static planner.model.leon.LeonQueryTemplate.GENERAL_CONDITIONS_ALL;
import static planner.model.leon.LeonQueryTemplate.GENERAL_CONDITIONS_ALL_AIRCRAFT_LIST;
import static planner.model.leon.LeonQueryTemplate.START_AIRPORT;

import java.time.LocalDate;
import planner.exception.LeonAccessException;
import planner.model.leon.LeonQueryBuilder;

public final class LeonUtil {
    public static String getValidatedResponse(String response) {
        if (response.contains(ERROR.value())) {
            throw new LeonAccessException("Check your Query, response contains error: " + response);
        }
        return response;
    }

    public static String prepareQueryAllActiveAircraft() {
        return getValidatedQuery(LeonQueryBuilder.builder()
                .filterName(FILTER_ALL_ACTIVE_AIRCRAFT.value())
                .generalConditions(GENERAL_CONDITIONS_ALL_AIRCRAFT_LIST.value())
                .build());
    }

    public static String prepareQueryAllFlightsByPeriod(Long daysRange) {
        String filterWithDate = FILTER_CONDITIONS_FLIGHT_LIST_ALL_AIRCRAFT.value()
                .replaceAll("START_DATE", LocalDate.now().atStartOfDay().toString());

        filterWithDate = filterWithDate.replaceAll("END_DATE",
                LocalDate.now().atStartOfDay().plusDays(daysRange).toString());

        return getValidatedQuery(LeonQueryBuilder.builder()
                .filterName(FILTER_FLIGHT_LIST.value())
                .filterCondition(filterWithDate)
                .generalConditions(GENERAL_CONDITIONS_ALL.value())
                .startAirport(START_AIRPORT.value())
                .endAirport(END_AIRPORT.value())
                .aircraft(AIRCRAFT.value())
                .crewList(CREW.value())
                .build());
    }

    private static String removeLineBreaks(String string) {
        return string
                .replaceAll("[\\r\\n]+", " ")
                .replaceAll(System.lineSeparator(), " ");
    }

    private static String getValidatedQuery(LeonQueryBuilder queryBuilder) {
        if (queryBuilder.getFilterName() == null || queryBuilder.getFilterName().isBlank()) {
            throw new LeonAccessException("Query must include filter to Leon DB");
        }
        return removeLineBreaks(queryBuilder.toString());
    }
}
