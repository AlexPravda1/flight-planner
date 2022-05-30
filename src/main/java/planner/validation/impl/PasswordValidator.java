package planner.validation.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanWrapperImpl;
import planner.model.dto.request.UserRegistrationDto;
import planner.validation.Password;

@Log4j2
public class PasswordValidator implements ConstraintValidator<Password, UserRegistrationDto> {
    private String field;
    private String fieldMatch;

    public void initialize(Password constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(UserRegistrationDto registrationDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        Object fieldValue = new BeanWrapperImpl(registrationDto)
                .getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(registrationDto)
                .getPropertyValue(fieldMatch);
        log.debug("PasswordValidator is checking if passwords are matching");
        return fieldValue != null && fieldValue.equals(fieldMatchValue);
    }
}
