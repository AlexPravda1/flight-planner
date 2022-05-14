package planner.util;

import static planner.model.leon.LeonQueryTemplate.AIRCRAFT;
import static planner.model.leon.LeonQueryTemplate.AIRCRAFT_ID;
import static planner.model.leon.LeonQueryTemplate.CREW_LIST;
import static planner.model.leon.LeonQueryTemplate.DEFAULT_DAYS_RANGE;
import static planner.model.leon.LeonQueryTemplate.END_AIRPORT;
import static planner.model.leon.LeonQueryTemplate.END_DAY;
import static planner.model.leon.LeonQueryTemplate.ERROR;
import static planner.model.leon.LeonQueryTemplate.FILTER_ALL_ACTIVE_AIRCRAFT;
import static planner.model.leon.LeonQueryTemplate.FILTER_CONDITIONS_FLIGHT_LIST_ALL_AIRCRAFT;
import static planner.model.leon.LeonQueryTemplate.FILTER_CONDITIONS_FLIGHT_LIST_BY_AIRCRAFT;
import static planner.model.leon.LeonQueryTemplate.FILTER_NAME_FLIGHT_LIST;
import static planner.model.leon.LeonQueryTemplate.GENERAL_CONDITIONS_ALL;
import static planner.model.leon.LeonQueryTemplate.GENERAL_CONDITIONS_ALL_AIRCRAFT_LIST;
import static planner.model.leon.LeonQueryTemplate.START_AIRPORT;
import static planner.model.leon.LeonQueryTemplate.START_DAY;

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

    public static String prepareQueryAllFlightsByPeriodAndAircraftId(
            Long daysRange, Long aircraftId) {
        String filterWithDateAndAicraft =
                getFilterByAircraftIdFromCurrentDatePlusRanges(daysRange, aircraftId);

        return getValidatedQuery(LeonQueryBuilder.builder()
                .filterName(FILTER_NAME_FLIGHT_LIST.value())
                .filterCondition(filterWithDateAndAicraft)
                .generalConditions(GENERAL_CONDITIONS_ALL.value())
                .startAirport(START_AIRPORT.value())
                .endAirport(END_AIRPORT.value())
                .aircraft(AIRCRAFT.value())
                .crewList(CREW_LIST.value())
                .build());
    }

    public static String prepareQueryAllFlightsByPeriod(Long daysRange) {
        String filterWithDate = getFilterAllAircraftFromCurrentDatePlusRange(daysRange);

        return getValidatedQuery(LeonQueryBuilder.builder()
                .filterName(FILTER_NAME_FLIGHT_LIST.value())
                .filterCondition(filterWithDate)
                .generalConditions(GENERAL_CONDITIONS_ALL.value())
                .startAirport(START_AIRPORT.value())
                .endAirport(END_AIRPORT.value())
                .aircraft(AIRCRAFT.value())
                .crewList(CREW_LIST.value())
                .build());
    }

    private static String getFilterAllAircraftFromCurrentDatePlusRange(Long daysRange) {
        if (daysRange == null || daysRange == 0) {
            daysRange = Long.parseLong(DEFAULT_DAYS_RANGE.value());
        }
        String filter = FILTER_CONDITIONS_FLIGHT_LIST_ALL_AIRCRAFT.value()
                .replaceAll(START_DAY.value(), LocalDate.now().atStartOfDay().toString());

        filter = filter.replaceAll(END_DAY.value(),
                LocalDate.now().atStartOfDay().plusDays(daysRange).toString());
        return filter;
    }

    private static String getFilterByAircraftIdFromCurrentDatePlusRanges(
            Long daysRange, Long aircraftId) {
        if (daysRange == null || daysRange == 0) {
            daysRange = Long.parseLong(DEFAULT_DAYS_RANGE.value());
        }
        String filter = FILTER_CONDITIONS_FLIGHT_LIST_BY_AIRCRAFT.value()
                .replaceAll(START_DAY.value(), LocalDate.now().atStartOfDay().toString());

        filter = filter.replaceAll(END_DAY.value(),
                LocalDate.now().atStartOfDay().plusDays(daysRange).toString());

        filter = filter.replaceAll(AIRCRAFT_ID.value(), String.valueOf(aircraftId));
        return filter;
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
