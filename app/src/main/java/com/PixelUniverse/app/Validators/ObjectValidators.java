package com.PixelUniverse.app.Validators;

import jakarta.validation.*;
import org.hibernate.mapping.Collection;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ObjectValidators<T> {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public Set<String> validate(T validObject){
        Set<ConstraintViolation<T>> violations = validator.validate(validObject);
        if (!violations.isEmpty()){
            return violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
