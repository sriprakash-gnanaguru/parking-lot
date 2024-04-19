package com.parking.nl.validator;

import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import com.parking.nl.exception.InvalidInputException;
import com.parking.nl.exception.ServiceException;
import com.parking.nl.service.TariffService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CheckInDateValidatorTest {
    @InjectMocks
    CheckInDateValidator validator;

    @Test
    @DisplayName("Validate the CheckInDate containing any future values")
    public void testValidate(){
        Assertions.assertDoesNotThrow(()->validator.validate(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Validate the CheckInDate with future values then result in exception")
    public void testValidateThenException(){
        Assertions.assertThrows(InvalidInputException.class, () -> validator.validate(LocalDateTime.now().plusMinutes(5)));
    }

    @Test
    @DisplayName("VValidate the CheckInDates containing any future value")
    public void testValidateWithParam(){
        Assertions.assertDoesNotThrow(()->validator.validate(Collections.singletonList(UnregisteredVehiclesRequest.builder().streetName("Java").checkInDateTime(LocalDateTime.now()).build())));
    }

    @Test
    @DisplayName("Validate the CheckInDates with future values then result in exception in the process")
    public void testValidatewithListParamThenException(){
        Assertions.assertThrows(InvalidInputException.class, () -> validator.validate(Collections.singletonList(UnregisteredVehiclesRequest.builder().checkInDateTime(LocalDateTime.now().plusMinutes(5)).build())));
    }

}
