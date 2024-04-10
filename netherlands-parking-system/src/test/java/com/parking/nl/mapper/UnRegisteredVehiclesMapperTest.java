package com.parking.nl.mapper;

import com.parking.nl.data.model.ParkingStatus;
import com.parking.nl.data.model.ParkingVehicle;
import com.parking.nl.data.model.UnRegsiteredVehicles;
import com.parking.nl.data.repository.UnRegisteredVehiclesRepository;
import com.parking.nl.domain.request.ParkingRequest;
import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnRegisteredVehiclesMapperTest {

    private UnRegisteredVehiclesMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UnRegisteredVehiclesMapper.class);
    }

    @Test
    @DisplayName("translate request into Model correctly")
    public void testTranslate() {
        UnregisteredVehiclesRequest request = UnregisteredVehiclesRequest.builder().licensePlateNumber("NL-GY-894").streetName("Azure").build();
        UnRegsiteredVehicles model = mapper.translate(request);
        assertEquals("NL-GY-894", model.getLicensePlateNumber());
        assertEquals("Azure", model.getStreetName());
        assertEquals(Boolean.FALSE, model.isNotified());
    }
}
