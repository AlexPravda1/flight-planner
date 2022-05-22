package planner.model.leon;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static planner.model.leon.LeonQueryTemplateBuilderConfig.BODY_POSTFIX;
import static planner.model.leon.LeonQueryTemplateBuilderConfig.BODY_PREFIX;
import static planner.model.leon.LeonQueryTemplateBuilderConfig.QUERY_POSTFIX;
import static planner.model.leon.LeonQueryTemplateBuilderConfig.QUERY_PREFIX;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LeonQuery {
    @Builder.Default
    private String queryPrefix = QUERY_PREFIX.value();

    private String filterName;

    @Builder.Default
    private String filterCondition = SPACE;

    @Builder.Default
    private String bodyPrefix = BODY_PREFIX.value();

    @Builder.Default
    private String generalConditions = SPACE;

    @Builder.Default
    private String flightWatch = SPACE;

    @Builder.Default
    private String aircraft = SPACE;

    @Builder.Default
    private String startAirport = SPACE;

    @Builder.Default
    private String endAirport = SPACE;

    @Builder.Default
    private String sectorHandling = SPACE;

    @Builder.Default
    private String checklist = SPACE;

    @Builder.Default
    private String notes = SPACE;

    @Builder.Default
    private String passengerList = SPACE;

    @Builder.Default
    private String crewList = SPACE;

    @Builder.Default
    private String bodyPostfix = BODY_POSTFIX.value();

    @Builder.Default
    private String queryPostfix = QUERY_POSTFIX.value();

    @Override
    public String toString() {
        return queryPrefix
                + filterName + filterCondition
                + bodyPrefix
                + generalConditions
                + flightWatch
                + aircraft
                + startAirport + endAirport
                + sectorHandling
                + checklist
                + notes
                + passengerList
                + crewList
                + bodyPostfix
                + queryPostfix
                .replaceAll("[\\r\\n]+", SPACE)
                .replaceAll(System.lineSeparator(), SPACE);
    }
}
