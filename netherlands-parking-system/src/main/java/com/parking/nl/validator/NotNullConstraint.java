package com.parking.nl.validator;

import com.parking.nl.common.Constants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = NotNullValidator.class)
@Documented
public @interface NotNullConstraint {

    String message() default Constants.NOT_NULL_VALIDATION_MSG;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
