package planner.model.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private List<RoleResponseDto> roles;
    private String email;
    private String name;
    private String surname;
    private long airlineId;
}
