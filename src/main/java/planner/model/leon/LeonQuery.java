package planner.model.leon;

import static planner.model.leon.LeonQueryTemplateBuilderConfig.BODY_POSTFIX;
import static planner.model.leon.LeonQueryTemplateBuilderConfig.BODY_PREFIX;
import static planner.model.leon.LeonQueryTemplateBuilderConfig.QUERY_POSTFIX;
import static planner.model.leon.LeonQueryTemplateBuilderConfig.QUERY_PREFIX;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Builder
@Getter
public class LeonQuery {
    @Builder.Default
    private String queryPrefix = QUERY_PREFIX.value();

    private String filterName;

    @Builder.Default
    private String filterCondition = StringUtils.SPACE;

    @Builder.Default
    private String bodyPrefix = BODY_PREFIX.value();

    @Builder.Default
    private String generalConditions = StringUtils.SPACE;

    @Builder.Default
    private String flightWatch = StringUtils.SPACE;

    @Builder.Default
    private String aircraft = StringUtils.SPACE;

    @Builder.Default
    private String startAirport = StringUtils.SPACE;

    @Builder.Default
    private String endAirport = StringUtils.SPACE;

    @Builder.Default
    private String sectorHandling = StringUtils.SPACE;

    @Builder.Default
    private String checklist = StringUtils.SPACE;

    @Builder.Default
    private String passengerList = StringUtils.SPACE;

    @Builder.Default
    private String crewList = StringUtils.SPACE;

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
                + passengerList
                + crewList
                + bodyPostfix
                + queryPostfix
                .replaceAll("[\\r\\n]+", StringUtils.SPACE)
                .replaceAll(System.lineSeparator(), StringUtils.SPACE);
    }
}
