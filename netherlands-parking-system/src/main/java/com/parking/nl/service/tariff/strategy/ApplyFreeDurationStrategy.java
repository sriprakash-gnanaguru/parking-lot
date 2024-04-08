package com.parking.nl.service.tariff.strategy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@Qualifier("freeDurationStrategy")
public class ApplyFreeDurationStrategy implements DurationStrategy{

    @Override
   public Duration calculateDuration(LocalDateTime start, LocalDateTime end) {
        long freeHours = 0;
        long freeMinutes = 0;
        LocalDateTime currentDateTime = null;
        for (currentDateTime = start; currentDateTime.isBefore(end.plusHours(1)); currentDateTime = currentDateTime.plusHours(1)) {
            DayOfWeek dayOfWeek = currentDateTime.getDayOfWeek();
            LocalTime currentTime = currentDateTime.toLocalTime();
            if (dayOfWeek == DayOfWeek.SUNDAY || (currentTime.isAfter(LocalTime.of(21, 0)) || currentTime.compareTo(LocalTime.of(7, 59)) <= 0)) {
                if (Duration.between(currentDateTime.minusHours(1), end).toMinutes() >= 60) {
                    freeHours++;
                } else {
                    freeMinutes = Duration.between(end, currentDateTime).toMinutes();
                }
            }
        }
        return Duration.ofHours(freeHours).plusMinutes(freeMinutes);
    }
}
