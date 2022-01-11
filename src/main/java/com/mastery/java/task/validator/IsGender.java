package com.mastery.java.task.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = GenderValidator.class)
public @interface IsGender {
    String message() default "Gender is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
