package org.design_manager_project.validations.validStatus;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidateStatus.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStatus {

    String message() default "Invalid Status";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
