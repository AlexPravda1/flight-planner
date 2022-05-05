package planner.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserLoginDto {
    @NotBlank(message = "Login can't be blank!")
    private String login;
    @NotBlank(message = "Password can't be blank!")
    private String password;
}
