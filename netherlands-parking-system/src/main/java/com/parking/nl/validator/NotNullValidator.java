package com.parking.nl.validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
public class NotNullValidator implements ConstraintValidator<NotNullConstraint, String> {
     @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
         if(value == null || value.isEmpty()){
             return false;
         }else{
             return true;
         }
    }
}
