package com.parking.nl.service.tariff;

import com.parking.nl.service.impl.TariffServiceImpl;
import com.parking.nl.service.tariff.strategy.ApplyFreeDurationStrategy;
import com.parking.nl.service.tariff.strategy.ApplyParkingDurationStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class TariffCalculatorTest {
    @Mock
    private TariffServiceImpl tariffService;
    @Mock
    private ApplyParkingDurationStrategy parkingStrategy;
    @Mock
    private ApplyFreeDurationStrategy freeStrategy;
    @InjectMocks
    TariffCalculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new TariffCalculator(parkingStrategy,freeStrategy,tariffService);
    }

    @Test
    @DisplayName("Calculating Tariff based on parking fee and free minutes")
    public void testcalculateTariff(){
        Mockito.when(freeStrategy.calculateDuration(any(),any())).thenReturn(30L);
        Mockito.when(parkingStrategy.calculateDuration(any(),any())).thenReturn(150L);
        Mockito.when(tariffService.loadTariffMetaData()).thenReturn(Collections.singletonMap("Java",BigDecimal.TEN));
        Assertions.assertEquals(new BigDecimal("12.00"),calculator.calculateTariff("Java",any(),any()));
    }

    @Test
    @DisplayName("Calculating Tariff based on parking fee and free minutes")
    public void testcalculateTariffWithException(){
        Mockito.when(freeStrategy.calculateDuration(any(),any())).thenReturn(30L);
        Mockito.when(parkingStrategy.calculateDuration(any(),any())).thenReturn(150L);
        Mockito.when(tariffService.loadTariffMetaData()).thenThrow(new RuntimeException("Unexected operartion"));
        Assertions.assertThrows( RuntimeException.class,() -> calculator.calculateTariff("Java",any(),any()));
    }



}
