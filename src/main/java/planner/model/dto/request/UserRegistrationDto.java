package planner.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import planner.validation.Password;

@Getter
@Setter
@NoArgsConstructor
@Password(field = "password", fieldMatch = "repeatPassword")
public class UserRegistrationDto {
    @Email
    @NotNull
    private String email;

    @NotEmpty(message = "The password couldn't be empty")
    @Size(min = 8, message = "Password must be at least 8 symbols long")
    @NotNull
    private String password;
    private String repeatPassword;

    @NotEmpty(message = "Please enter your name")
    @Size(min = 3, message = "Name must be at least 3 symbols long")
    @NotNull
    private String name;

    @NotEmpty(message = "Please enter your family name")
    @Size(min = 3, message = "Family name must be at least 3 symbols long")
    @NotNull
    private String surname;
}
