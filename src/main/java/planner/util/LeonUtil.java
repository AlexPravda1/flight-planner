package planner.util;

import static planner.model.leon.LeonQueryTemplateBuilderConfig.ERROR;
import static planner.model.leon.LeonQueryTemplateBuilderConfig.RESPONSE_FIX_UTC_CAMEL_CASE;
import static planner.model.leon.LeonQueryTemplateBuilderConfig.RESPONSE_UTC_CAPITAL;
import static planner.model.leon.LeonQueryTemplateFilterConditions.AIRCRAFT_ID;
import static planner.model.leon.LeonQueryTemplateFilterConditions.CONDITIONS_FLIGHT_LIST_ALL_AIRCRAFT;
import static planner.model.leon.LeonQueryTemplateFilterConditions.CONDITIONS_FLIGHT_LIST_BY_AIRCRAFT;
import static planner.model.leon.LeonQueryTemplateFilterConditions.DEFAULT_DAYS_RANGE;
import static planner.model.leon.LeonQueryTemplateFilterConditions.END_DAY;
import static planner.model.leon.LeonQueryTemplateFilterConditions.GENERAL_CONDITIONS_ALL;
import static planner.model.leon.LeonQueryTemplateFilterConditions.GENERAL_CONDITIONS_ALL_ACTIVE_AIRCRAFT_LIST;
import static planner.model.leon.LeonQueryTemplateFilterConditions.START_DAY;
import static planner.model.leon.LeonQueryTemplateFilterType.FILTER_TYPE_ALL_AIRCRAFT;
import static planner.model.leon.LeonQueryTemplateFilterType.FILTER_TYPE_FLIGHT_LIST;
import static planner.model.leon.LeonQueryTemplateRequestAircraft.AIRCRAFT;
import static planner.model.leon.LeonQueryTemplateRequestAirport.END_AIRPORT;
import static planner.model.leon.LeonQueryTemplateRequestAirport.START_AIRPORT;
import static planner.model.leon.LeonQueryTemplateRequestCrew.CREW_LIST;

import java.time.LocalDate;
import planner.exception.LeonAccessException;
import planner.model.leon.LeonQuery;
import planner.model.leon.LeonQueryTemplateFilterConditions;

public final class LeonUtil {
    public static void validateJsonResponse(String response) {
        if (response.contains(ERROR.value())) {
            throw new LeonAccessException("Check your Query, response contains error: "
                    + response);
        }
    }

    public static String fixJsonForMappers(String response) {
        return response
                .replaceAll(RESPONSE_UTC_CAPITAL.value(), RESPONSE_FIX_UTC_CAMEL_CASE.value());
    }

    public static String prepareQueryAllAircraft() {
        LeonQuery queryBuilder = LeonQuery.builder()
                .filterName(FILTER_TYPE_ALL_AIRCRAFT.value())
                .generalConditions(GENERAL_CONDITIONS_ALL_ACTIVE_AIRCRAFT_LIST.value())
                .build();
        validateQueryBuilder(queryBuilder);
        return queryBuilder.toString();
    }

    public static String prepareQueryAllFlightsByPeriodAndAircraftId(
            Long daysRange, Long aircraftId) {
        String filterWithDateAndAircraft =
                getFilterByAircraftIdFromCurrentDatePlusRange(daysRange, aircraftId);

        LeonQuery queryBuilder = LeonQuery.builder()
                .filterName(FILTER_TYPE_FLIGHT_LIST.value())
                .filterCondition(filterWithDateAndAircraft)
                .generalConditions(GENERAL_CONDITIONS_ALL.value())
                .startAirport(START_AIRPORT.value())
                .endAirport(END_AIRPORT.value())
                .aircraft(AIRCRAFT.value())
                .crewList(CREW_LIST.value())
                .build();
        validateQueryBuilder(queryBuilder);
        return queryBuilder.toString();
    }

    public static String prepareQueryAllFlightsByPeriod(Long daysRange) {
        String filterWithDate = getFilterAllAircraftFromCurrentDatePlusRange(daysRange);

        LeonQuery queryBuilder = LeonQuery.builder()
                .filterName(FILTER_TYPE_FLIGHT_LIST.value())
                .filterCondition(filterWithDate)
                .generalConditions(GENERAL_CONDITIONS_ALL.value())
                .startAirport(START_AIRPORT.value())
                .endAirport(END_AIRPORT.value())
                .aircraft(AIRCRAFT.value())
                .build();
        validateQueryBuilder(queryBuilder);
        return queryBuilder.toString();
    }

    private static void validateQueryBuilder(LeonQuery queryBuilder) {
        if (queryBuilder.getFilterName() == null || queryBuilder.getFilterName().isBlank()) {
            throw new LeonAccessException("Query must include filter to Leon DB");
        }
    }

    private static String getFilterAllAircraftFromCurrentDatePlusRange(Long daysRange) {
        return putDateInQueryTemplate(CONDITIONS_FLIGHT_LIST_ALL_AIRCRAFT, daysRange);
    }

    private static String getFilterByAircraftIdFromCurrentDatePlusRange(
            Long daysRange, Long aircraftId) {
        return putDateInQueryTemplate(CONDITIONS_FLIGHT_LIST_BY_AIRCRAFT, daysRange)
                .replaceAll(AIRCRAFT_ID.value(), String.valueOf(aircraftId));
    }

    private static String putDateInQueryTemplate(LeonQueryTemplateFilterConditions filterTemplate,
                                                 Long daysRange) {
        if (daysRange == null || daysRange == 0) {
            daysRange = Long.parseLong(DEFAULT_DAYS_RANGE.value());
        }
        String filterWithStartDate = filterTemplate.value()
                .replaceAll(START_DAY.value(), LocalDate.now().atStartOfDay().toString());

        return filterWithStartDate.replaceAll(END_DAY.value(),
                LocalDate.now().atStartOfDay().plusDays(daysRange).toString());
    }
}
