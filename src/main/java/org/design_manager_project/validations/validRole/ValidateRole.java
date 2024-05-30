package org.design_manager_project.validations.validRole;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.design_manager_project.models.enums.Role;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class ValidateRole implements ConstraintValidator<ValidRole, String> {
    @Override
    public void initialize(ValidRole constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        for (var value: Role.values()){
            if (Objects.equals(s, String.valueOf(Role.HOST))){
                return false;
            }else if (Objects.equals(s, value.name())){
                return true;
            }
        }
        return false;
    }
}
