package com.mastery.java.task.validator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {AdultValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Adult {
    String message() default "Person is not an adult";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
