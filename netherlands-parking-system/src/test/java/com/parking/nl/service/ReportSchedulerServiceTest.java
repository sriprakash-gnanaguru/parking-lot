package com.parking.nl.service;

import com.parking.nl.data.model.UnRegsiteredVehicles;
import com.parking.nl.data.repository.UnRegisteredVehiclesRepository;
import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import com.parking.nl.mapper.UnRegisteredVehiclesMapper;
import com.parking.nl.service.impl.ReportSchedulerServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

@ExtendWith(SpringExtension.class)
public class ReportSchedulerServiceTest {
    @Mock
    private UnRegisteredVehiclesRepository repository;

    @Mock
    private UnRegisteredVehiclesMapper mapper;
    @InjectMocks
    ReportSchedulerServiceImpl service;

    @Test
    @DisplayName("Positive Scenario in the scheduler while persisting the report")
    public void testSave(){
        UnregisteredVehiclesRequest request = UnregisteredVehiclesRequest.builder().licensePlateNumber("NL-MU-098").checkInDateTime(LocalDateTime.now()).streetName("Azure").build();
        Mockito.when(mapper.translate(any())).thenReturn(UnRegsiteredVehicles.builder().build());
        service.save(Collections.singletonList(mapper.translate(request)));
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Negative Scenario in the scheduler with")
    public void testSaveWithNullPointer(){
        UnregisteredVehiclesRequest request = UnregisteredVehiclesRequest.builder().licensePlateNumber("NL-MU-098").checkInDateTime(LocalDateTime.now()).streetName("Azure").build();
        Mockito.when(mapper.translate(any())).thenThrow(new NullPointerException());
        Assertions.assertThrows(NullPointerException.class, () -> service.save(Collections.singletonList(mapper.translate(request))));
    }

    @Test
    @DisplayName("Positive Scenario to check whether penalty report is generated or not ")
    public void testfindByIsNotified(){
        Mockito.when(repository.findByisNotified(any())).thenReturn(anyList());
        Assertions.assertNotNull(service.findByIsNotified(Boolean.FALSE));
    }

    @Test
    @DisplayName("Negative Scenario to check whether penalty report with exception in the process ")
    public void testfindByIsNotifiedwithException(){
        Mockito.when(repository.findByisNotified(any())).thenThrow(new RuntimeException());
        Assertions.assertThrows(RuntimeException.class, () -> service.findByIsNotified(Boolean.FALSE));
    }


}
