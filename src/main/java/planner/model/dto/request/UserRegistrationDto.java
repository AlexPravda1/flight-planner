package planner.model.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import planner.validation.Password;

@Data
@Password(field = "password", fieldMatch = "repeatPassword")
public class UserRegistrationDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank(message = "The password couldn't be empty")
    @Size(min = 8, message = "Password must be at least 8 symbols long")
    private String password;
    private String repeatPassword;

    @NotBlank(message = "Please enter your name")
    @Size(min = 3, message = "Name must be at least 3 symbols long")
    private String name;

    @NotBlank(message = "Please enter your family name")
    @Size(min = 3, message = "Family name must be at least 3 symbols long")
    private String surname;
}
