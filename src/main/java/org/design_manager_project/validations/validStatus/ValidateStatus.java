package org.design_manager_project.validations.validStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.design_manager_project.models.enums.Status;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class ValidateStatus implements ConstraintValidator<ValidStatus, String> {
    @Override
    public void initialize(ValidStatus constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        for (var value: Status.values()){
            if (Objects.equals(s, value.name())){
                return true;
            }
        }
        return false;
    }
}
