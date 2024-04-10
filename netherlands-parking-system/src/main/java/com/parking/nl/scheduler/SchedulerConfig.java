package com.parking.nl.scheduler;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class SchedulerConfig {

     @Value("${report.location}")
    private String reportLocation;

    @Value("${report.cron}")
    private String cronExpression;
}
