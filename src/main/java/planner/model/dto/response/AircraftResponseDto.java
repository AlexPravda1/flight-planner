package planner.model.dto.response;

import lombok.Data;

@Data
public class AircraftResponseDto {
    private Long id;
    private String registration;
    private String type;
    private String airlineName;
}
