package com.parking.nl.service.tariff.strategy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@Qualifier("parkingDurationStrategy")
public class ApplyParkingDurationStrategy implements DurationStrategy{
    @Override
    public long calculateDuration(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }
}
