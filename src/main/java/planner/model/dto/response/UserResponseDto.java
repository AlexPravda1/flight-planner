package planner.model.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private List<RoleResponseDto> roles;
    private String email;
    private String name;
    private String surname;
}
