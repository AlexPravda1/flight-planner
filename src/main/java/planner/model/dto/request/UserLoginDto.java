package planner.model.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginDto {
    @NotBlank(message = "Login can't be blank!")
    @Size(min = 3, message = "Login must be at least 3 symbols long")
    private String login;
    @NotBlank(message = "Password can't be blank!")
    @Size(min = 8, message = "Password must be at least 8 symbols long")
    private String password;
}
