package com.parking.nl.service.tariff;

import com.parking.nl.exception.InvalidInputException;
import com.parking.nl.service.TariffService;
import com.parking.nl.service.tariff.strategy.DurationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class TariffCalculator {
    private final DurationStrategy parkingStrategy;
    private final DurationStrategy freeStrategy;
    private TariffService tariffService;

    @Autowired
    public TariffCalculator( @Qualifier("parkingDurationStrategy")final DurationStrategy parkingStrategy, @Qualifier("freeDurationStrategy") final DurationStrategy freeStrategy,TariffService tariffService){
        this.parkingStrategy = parkingStrategy;
        this.freeStrategy = freeStrategy;
        this.tariffService =  tariffService;
    }

    public BigDecimal calculateTariff(String streetName, LocalDateTime startTime, LocalDateTime endTime){
        long payable = parkingStrategy.calculateDuration(startTime,endTime) - freeStrategy.calculateDuration(startTime,endTime);
        BigDecimal tariff = BigDecimal.valueOf(tariffService.loadTariffMetaData().get(streetName));
        if(tariff == null){
            throw new InvalidInputException("No Tariff is defined for the steeet:"+streetName);
        }
        BigDecimal costinCents = tariff.multiply(new BigDecimal(payable));
        return costinCents.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

}

