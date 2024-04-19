package com.parking.nl.service;

import com.parking.nl.data.model.Street;
import com.parking.nl.data.model.UnRegsiteredVehicles;
import com.parking.nl.data.repository.StreetRepository;
import com.parking.nl.data.repository.UnRegisteredVehiclesRepository;
import com.parking.nl.domain.request.StreetRequest;
import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import com.parking.nl.exception.InvalidInputException;
import com.parking.nl.exception.ServiceException;
import com.parking.nl.helper.PenaltyHelper;
import com.parking.nl.mapper.UnRegisteredVehiclesMapper;
import com.parking.nl.service.impl.AdminRegisterServiceImpl;
import com.parking.nl.service.tariff.TariffCalculator;
import com.parking.nl.validator.CheckInDateValidator;
import com.parking.nl.validator.StreetValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

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
    private CheckInDateValidator checkInDateValidator;
   @Mock
   private PenaltyHelper helper;
   @InjectMocks
    private AdminRegisterServiceImpl service;
    @Mock
    private StreetRepository streetRepository;
    @Mock
    private TariffService tariffService;
    @Mock
    private TariffCalculator tariffCalculator;

    @Test
    @DisplayName("Positive Scenario to test the save operation of Unregisterised vehicles in the street")
    public void testPersistUnregisterVehicles(){
        UnregisteredVehiclesRequest request = UnregisteredVehiclesRequest.builder().licensePlateNumber("NL-MU-098").checkInDateTime(LocalDateTime.now()).streetName("Azure").build();
        Mockito.when(tariffService.loadTariffMetaData()).thenReturn(Collections.singletonMap("Java", BigDecimal.ONE));
        doNothing().when(streetValidator).validate(Collections.singletonList(request));
        doNothing().when(checkInDateValidator).validate(Collections.singletonList(request));
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

    @Test
    @DisplayName("Positive Scenario to test the save operation of streets")
    public void testAddStreets(){
        StreetRequest request = StreetRequest.builder().price(new BigDecimal(10.75)).streetName("Azure").build();
        Mockito.when(streetRepository.findByNameIgnoreCase(request.getStreetName())).thenReturn(Optional.empty());
        service.addStreets(Collections.singletonList(request));
        Mockito.verify(streetRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Negative Scenario to test the existing street in the system.")
    public void testAddStreetsWithExistStreet(){
        StreetRequest request = StreetRequest.builder().price(new BigDecimal(10.75)).streetName("Azure").build();
        Mockito.when(streetRepository.findByNameIgnoreCase(request.getStreetName())).thenReturn(Optional.of(Street.builder().name("Azure").build()));
        Assertions.assertThrows(ServiceException.class, () -> service.addStreets(Collections.singletonList(request)));
    }
}
