package com.cheong.wcc_assessment.location.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingFullPostcodeValidator.class)
public @interface ExistingFullPostcode {
    String message() default "Postcode is already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
