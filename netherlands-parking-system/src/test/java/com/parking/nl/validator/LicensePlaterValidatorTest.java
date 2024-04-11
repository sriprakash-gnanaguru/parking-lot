package com.parking.nl.validator;

import com.parking.nl.exception.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class LicensePlaterValidatorTest {
    @InjectMocks
    LicensePlateValidator validator;

    @Test
    @DisplayName("Validate the license plate number and throw exception if null or empty")
    public void testValidate(){
        Assertions.assertThrows(InvalidInputException.class, () -> validator.validate(new String()));
    }
}
