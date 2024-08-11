package com.cheong.wcc_assessment.location.validator;

import jakarta.validation.ValidationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class LocationValidator {

    private final Validator validator;

    public LocationValidator(Validator validator) {
        this.validator = validator;
    }

    public boolean validateOutcode(Object object) {
        Errors errors = validator.validateObject(object);

        if (!errors.hasErrors()) {
            return true;
        }

        List<String> errorMessages = errors.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        throw new ValidationException(String.join(",", errorMessages));
    }

}
