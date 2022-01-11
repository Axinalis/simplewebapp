package com.mastery.java.task.validator;

import com.mastery.java.task.dto.Gender;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class GenderValidator implements ConstraintValidator<IsGender, String> {

    @Override
    public boolean isValid(String gender, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(Gender.values())
                .anyMatch(enumGender -> enumGender.name().equalsIgnoreCase(gender));
    }
}
