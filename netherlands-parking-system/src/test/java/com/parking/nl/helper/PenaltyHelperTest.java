package com.parking.nl.helper;

import com.parking.nl.data.model.ParkingVehicle;
import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import com.parking.nl.exception.InvalidInputException;
import com.parking.nl.service.ParkingSystemService;
import org.hamcrest.Matchers;
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
import java.util.List;

@ExtendWith(SpringExtension.class)
public class PenaltyHelperTest {
    @Mock
    private ParkingSystemService service;
    @InjectMocks
    PenaltyHelper helper;

    @Test
    @DisplayName("Positive scenario - validating whether unregistered vehicles has active parking session")
    public void testGetPenaltyVehicles(){
        Mockito.when(service.findLicensePlateNumberByStatus(Mockito.any())).thenReturn(Collections.singletonList(ParkingVehicle.builder().licensePlateNumber("NL-JK-76").build()));
        List<UnregisteredVehiclesRequest> requestList = helper.getPenaltyVehicles((Collections.singletonList(UnregisteredVehiclesRequest.builder().streetName("Azure").licensePlateNumber("NK-UI-963").checkInDateTime(LocalDateTime.now()).build())));
        Assertions.assertEquals(requestList.size(), 1);
    }

    @Test
    @DisplayName("Negative Scenario - validating whether unregistered vehicles has active parking session with exception")
    public void testGetPenaltyVehiclesWithException(){
        Mockito.when(service.findLicensePlateNumberByStatus(Mockito.any())).thenThrow(new InvalidInputException("Vehicle has active parking session"));
        Assertions.assertThrows(InvalidInputException.class, () ->helper.getPenaltyVehicles((Collections.singletonList(UnregisteredVehiclesRequest.builder().build()))));
    }


}
