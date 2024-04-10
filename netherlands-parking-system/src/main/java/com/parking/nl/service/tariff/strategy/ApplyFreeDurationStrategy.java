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
   public long calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {
        DayOfWeek dayOfWeek = startTime.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SUNDAY){
            return Duration.between(startTime, endTime).toMinutes();
        }else{
            long freeMinutes = 0;
            while (startTime.isBefore(endTime)) {
                if ((startTime.toLocalTime().isBefore(LocalTime.of(7, 59))
                        || startTime.toLocalTime().isAfter(LocalTime.of(21, 0)))) {
                    freeMinutes++;
                }
                startTime = startTime.plusMinutes(1);
            }
            return freeMinutes;
        }

    }
}
