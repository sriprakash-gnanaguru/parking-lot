package com.parking.nl.service;

import com.parking.nl.data.model.UnRegsiteredVehicles;
import com.parking.nl.data.repository.UnRegisteredVehiclesRepository;
import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import com.parking.nl.exception.InvalidInputException;
import com.parking.nl.helper.PenaltyHelper;
import com.parking.nl.mapper.UnRegisteredVehiclesMapper;
import com.parking.nl.service.impl.AdminRegisterServiceImpl;
import com.parking.nl.validator.StreetValidator;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(SpringExtension.class)
public class AdminRegisterServiceTest {
   @Mock
   private UnRegisteredVehiclesRepository repository;
   @Mock
   private UnRegisteredVehiclesMapper mapper;
   @Mock
   private StreetValidator streetValidator;
   @Mock
   private PenaltyHelper helper;
   @InjectMocks
    private AdminRegisterServiceImpl service;

    @Test
    @DisplayName("Positive Scenario to test the save operation of Unregisterised vehicles in the street")
    public void testPersistUnregisterVehicles(){
        UnregisteredVehiclesRequest request = UnregisteredVehiclesRequest.builder().licensePlateNumber("NL-MU-098").checkInDateTime(LocalDateTime.now()).streetName("Azure").build();
        doNothing().when(streetValidator).validate(Collections.singletonList(request));
        Mockito.when(helper.getPenaltyVehicles(Collections.singletonList(request))).thenReturn(Collections.singletonList(request));
        Mockito.when(mapper.translate(any())).thenReturn(any(UnRegsiteredVehicles.class));
        service.persistUnregisterVehicles(Collections.singletonList(request));
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Negative Scenario to test the save operation of Unregisterised vehicles in the street with exception")
    public void testPersistUnregisterVehiclesWithInvalidStreet() {
        UnregisteredVehiclesRequest request = UnregisteredVehiclesRequest.builder().licensePlateNumber("NL-MU-098").checkInDateTime(LocalDateTime.now()).streetName("Torreslaan").build();
        doThrow(new InvalidInputException("Street name is found in the street")).when(streetValidator).validate(Collections.singletonList(request));
        Assertions.assertThrows(InvalidInputException.class, () -> service.persistUnregisterVehicles(Collections.singletonList(request)));
    }
}
