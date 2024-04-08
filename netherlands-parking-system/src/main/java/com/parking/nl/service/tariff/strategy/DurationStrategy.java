package com.parking.nl.service.tariff.strategy;

import java.time.Duration;
import java.time.LocalDateTime;

public interface DurationStrategy {

    Duration calculateDuration(LocalDateTime start, LocalDateTime end);
}
