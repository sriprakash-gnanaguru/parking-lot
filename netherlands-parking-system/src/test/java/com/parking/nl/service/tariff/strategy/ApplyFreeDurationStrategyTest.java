package com.parking.nl.service.tariff.strategy;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ExtendWith(SpringExtension.class)
public class ApplyFreeDurationStrategyTest {
    @InjectMocks
    private ApplyFreeDurationStrategy strategy;
    private LocalDateTime afterTwentyOne;
    private LocalDateTime sunday;
    private LocalDateTime BeforeEight;

    private LocalDateTime normal;

    @BeforeEach
    public void init() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        afterTwentyOne = LocalDateTime.parse("2024-04-10 22:00:00.000", formatter);
        sunday = LocalDateTime.parse("2024-04-14 12:00:00.000", formatter);
        BeforeEight = LocalDateTime.parse("2024-04-10 02:00:00.000", formatter);
        BeforeEight = LocalDateTime.parse("2024-04-10 02:00:00.000", formatter);
        normal  = LocalDateTime.parse("2024-04-10 09:55:00.000", formatter);
    }

    @Test
    @DisplayName("All possible scenario in one testcase with various combination of dates")
    public void testcalculateDuration(){
        Assertions.assertEquals(5,strategy.calculateDuration(afterTwentyOne, afterTwentyOne.plusMinutes(5)));
        Assertions.assertEquals(120,strategy.calculateDuration(sunday, sunday.plusHours(2)));
        Assertions.assertEquals(45,strategy.calculateDuration(BeforeEight, BeforeEight.plusMinutes(45)));
        Assertions.assertEquals(0,strategy.calculateDuration(normal, normal.plusMinutes(30)));
    }
}
