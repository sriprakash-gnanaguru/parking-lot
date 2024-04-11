package com.parking.nl.scheduler;

import com.parking.nl.data.model.UnRegsiteredVehicles;
import com.parking.nl.service.ReportSchedulerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;

@ExtendWith(SpringExtension.class)
public class ReportSchedulerTest {
    @Mock
    private ReportSchedulerService service;
    @Mock
    private SchedulerConfig config;
    @InjectMocks
    ReportScheduler scheduler;

    @DisplayName("Positive scenario invoking the scheduler to perform the task")
    @Test
    public void testPeform(){
        Mockito.when(service.findByIsNotified(Mockito.any())).thenReturn(Collections.singletonList(UnRegsiteredVehicles.builder().licensePlateNumber("NL-KI-976").streetName("Azure").observationTime(LocalDateTime.now()).isNotified(Boolean.FALSE).build()));
        Mockito.when(config.getReportLocation()).thenReturn("C:\\Temp\\Reports");
        scheduler.perform();
        Mockito.verify(service, Mockito.times(1)).findByIsNotified(Mockito.any());
    }

    @DisplayName("Negative scenario invoking the scheduler ended with runtime exception")
    @Test
    public void testPeformThenException(){
        Mockito.when(service.findByIsNotified(Mockito.any())).thenThrow(new RuntimeException("Unexpected runtime exception"));
       Assertions.assertThrows(RuntimeException.class,()-> scheduler.perform());
    }
}
