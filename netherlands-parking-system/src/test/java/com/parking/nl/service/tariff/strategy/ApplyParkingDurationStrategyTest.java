package com.parking.nl.service.tariff.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
public class ApplyParkingDurationStrategyTest {
    @InjectMocks
    ApplyParkingDurationStrategy strategy;

    @Test
    public void testcalculateDuration(){
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(5);
        Assertions.assertEquals(5,strategy.calculateDuration(start, end));
    }
}
