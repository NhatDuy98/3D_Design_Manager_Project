package org.design_manager_project.validations.validRole;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidateRole.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRole {

    String message() default "Invalid Role";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
