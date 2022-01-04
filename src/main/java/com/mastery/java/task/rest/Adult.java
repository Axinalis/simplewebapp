package com.mastery.java.task.rest;

import javax.validation.Constraint;
import org.jvnet.staxex.StAxSOAPBody;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {AdultValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Adult {
    String message() default "Person is not an adult";
    Class<?>[] groups() default { };
    Class<? extends StAxSOAPBody.Payload>[] payload() default { };
}
