package planner.validation.impl;

import static model.UserHardcoded.getUserNoRolesNoId;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import planner.AbstractTest;
import planner.model.dto.request.UserRegistrationDto;
import planner.validation.Password;

class PasswordValidatorTest extends AbstractTest {
    private String userPassword;
    private UserRegistrationDto registrationDto;
    private PasswordValidator passwordValidator;
    @Mock private ConstraintValidatorContext constraintValidatorContext;
    @Mock private Password constraintAnnotation;

    @BeforeEach
    void setUp() {
        registrationDto = new UserRegistrationDto();
        registrationDto.setPassword(userPassword = getUserNoRolesNoId().getPassword());

        passwordValidator = new PasswordValidator();
        when(constraintAnnotation.field()).thenReturn("password");
        when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    void isValid_givenValidPassword_thenSuccess() {
        registrationDto.setRepeatPassword(userPassword);
        assertTrue(passwordValidator.isValid(registrationDto, constraintValidatorContext),
                "Passwords should match");
    }

    @Test
    void isValid_givenWrongPassword_thenFail() {
        registrationDto.setRepeatPassword(userPassword + SPACE);
        assertFalse(passwordValidator.isValid(registrationDto, constraintValidatorContext),
                "Passwords should NOT match");
    }
}
