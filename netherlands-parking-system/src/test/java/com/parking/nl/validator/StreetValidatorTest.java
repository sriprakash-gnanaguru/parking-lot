package com.parking.nl.validator;

import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import com.parking.nl.exception.InvalidInputException;
import com.parking.nl.service.TariffService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class StreetValidatorTest {
    @Mock
    private TariffService tariffService;
    @InjectMocks
    StreetValidator streetValidator;

    @Test
    @DisplayName("Validate the street name in the system")
    public void testValidate(){
        when(tariffService.loadTariffMetaData()).thenReturn(Collections.singletonMap("Java" , BigDecimal.ONE));
        streetValidator.validate("Java");
        verify(tariffService, times(1)).loadTariffMetaData();
    }

    @Test
    @DisplayName("Validate the street name in the system with exception")
    public void testValidateThenException(){
        when(tariffService.loadTariffMetaData()).thenThrow(new InvalidInputException("Street name is not found in the street"));
        Assertions.assertThrows(InvalidInputException.class, () -> streetValidator.validate("Java"));
    }

    @Test
    @DisplayName("Validate the street name in the system")
    public void testValidateWithParam(){
        when(tariffService.loadTariffMetaData()).thenReturn(Collections.singletonMap("Java" ,BigDecimal.ONE));
        streetValidator.validate(Collections.singletonList(UnregisteredVehiclesRequest.builder().streetName("Java").build()));
        verify(tariffService, times(1)).loadTariffMetaData();
    }

    @Test
    @DisplayName("Validate the street name in the system with exception")
    public void testValidatewithListParamThenException(){
        when(tariffService.loadTariffMetaData()).thenThrow(new InvalidInputException("Street name is not found in the street"));
        Assertions.assertThrows(InvalidInputException.class, () -> streetValidator.validate(Collections.singletonList(UnregisteredVehiclesRequest.builder().build())));
    }

}
