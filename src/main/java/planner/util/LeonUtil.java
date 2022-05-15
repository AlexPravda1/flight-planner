package planner.util;

import static planner.model.leon.LeonQueryTemplate.AIRCRAFT;
import static planner.model.leon.LeonQueryTemplate.AIRCRAFT_ID;
import static planner.model.leon.LeonQueryTemplate.CREW_LIST;
import static planner.model.leon.LeonQueryTemplate.DEFAULT_DAYS_RANGE;
import static planner.model.leon.LeonQueryTemplate.END_AIRPORT;
import static planner.model.leon.LeonQueryTemplate.END_DAY;
import static planner.model.leon.LeonQueryTemplate.ERROR;
import static planner.model.leon.LeonQueryTemplate.FILTER_CONDITIONS_FLIGHT_LIST_ALL_AIRCRAFT;
import static planner.model.leon.LeonQueryTemplate.FILTER_CONDITIONS_FLIGHT_LIST_BY_AIRCRAFT;
import static planner.model.leon.LeonQueryTemplate.FILTER_TYPE_ALL_ACTIVE_AIRCRAFT;
import static planner.model.leon.LeonQueryTemplate.FILTER_TYPE_FLIGHT_LIST;
import static planner.model.leon.LeonQueryTemplate.GENERAL_CONDITIONS_ALL;
import static planner.model.leon.LeonQueryTemplate.GENERAL_CONDITIONS_ALL_ACTIVE_AIRCRAFT_LIST;
import static planner.model.leon.LeonQueryTemplate.START_AIRPORT;
import static planner.model.leon.LeonQueryTemplate.START_DAY;

import java.time.LocalDate;
import planner.exception.LeonAccessException;
import planner.model.leon.LeonQueryBuilder;
import planner.model.leon.LeonQueryTemplate;

public final class LeonUtil {
    public static String getValidatedResponse(String response) {
        if (response.contains(ERROR.value())) {
            throw new LeonAccessException("Check your Query, response contains error: " + response);
        }
        return response;
    }

    public static String prepareQueryAllActiveAircraft() {
        LeonQueryBuilder queryBuilder = LeonQueryBuilder.builder()
                .filterName(FILTER_TYPE_ALL_ACTIVE_AIRCRAFT.value())
                .generalConditions(GENERAL_CONDITIONS_ALL_ACTIVE_AIRCRAFT_LIST.value())
                .build();
        return getValidatedQueryString(queryBuilder);
    }

    public static String prepareQueryAllFlightsByPeriodAndAircraftId(
            Long daysRange, Long aircraftId) {
        String filterWithDateAndAicraft =
                getFilterByAircraftIdFromCurrentDatePlusRange(daysRange, aircraftId);

        LeonQueryBuilder queryBuilder = LeonQueryBuilder.builder()
                .filterName(FILTER_TYPE_FLIGHT_LIST.value())
                .filterCondition(filterWithDateAndAicraft)
                .generalConditions(GENERAL_CONDITIONS_ALL.value())
                .startAirport(START_AIRPORT.value())
                .endAirport(END_AIRPORT.value())
                .aircraft(AIRCRAFT.value())
                .crewList(CREW_LIST.value())
                .build();
        return getValidatedQueryString(queryBuilder);
    }

    public static String prepareQueryAllFlightsByPeriod(Long daysRange) {
        String filterWithDate = getFilterAllAircraftFromCurrentDatePlusRange(daysRange);

        LeonQueryBuilder queryBuilder = LeonQueryBuilder.builder()
                .filterName(FILTER_TYPE_FLIGHT_LIST.value())
                .filterCondition(filterWithDate)
                .generalConditions(GENERAL_CONDITIONS_ALL.value())
                .startAirport(START_AIRPORT.value())
                .endAirport(END_AIRPORT.value())
                .aircraft(AIRCRAFT.value())
                .build();
        return getValidatedQueryString(queryBuilder);
    }

    private static String getValidatedQueryString(LeonQueryBuilder queryBuilder) {
        if (queryBuilder.getFilterName() == null || queryBuilder.getFilterName().isBlank()) {
            throw new LeonAccessException("Query must include filter to Leon DB");
        }
        return removeLineBreaks(queryBuilder.toString());
    }

    private static String getFilterAllAircraftFromCurrentDatePlusRange(Long daysRange) {
        return putDateInQueryTemplate(FILTER_CONDITIONS_FLIGHT_LIST_ALL_AIRCRAFT, daysRange);
    }

    private static String getFilterByAircraftIdFromCurrentDatePlusRange(
            Long daysRange, Long aircraftId) {
        String filterWithDate =
                putDateInQueryTemplate(FILTER_CONDITIONS_FLIGHT_LIST_BY_AIRCRAFT, daysRange);
        return filterWithDate.replaceAll(AIRCRAFT_ID.value(), String.valueOf(aircraftId));
    }

    private static String putDateInQueryTemplate(LeonQueryTemplate filterTemplate, Long daysRange) {
        if (daysRange == null || daysRange == 0) {
            daysRange = Long.parseLong(DEFAULT_DAYS_RANGE.value());
        }
        String filterWithStartDate = filterTemplate.value()
                .replaceAll(START_DAY.value(), LocalDate.now().atStartOfDay().toString());

        return filterWithStartDate.replaceAll(END_DAY.value(),
                LocalDate.now().atStartOfDay().plusDays(daysRange).toString());
    }

    private static String removeLineBreaks(String string) {
        return string
                .replaceAll("[\\r\\n]+", " ")
                .replaceAll(System.lineSeparator(), " ");
    }
}
