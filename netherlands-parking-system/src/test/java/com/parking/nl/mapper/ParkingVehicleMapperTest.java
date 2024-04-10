package com.parking.nl.mapper;

import com.parking.nl.data.model.ParkingStatus;
import com.parking.nl.data.model.ParkingVehicle;
import com.parking.nl.domain.request.ParkingRequest;
import com.parking.nl.service.ParkingSystemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingVehicleMapperTest {
    private ParkingVehiclesMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ParkingVehiclesMapper.class);
    }

    @Test
    @DisplayName("translate request into Model correctly")
    public void testTranslate() {
        ParkingRequest request = ParkingRequest.builder().licensePlateNumber("NL-GY-894").streetName("Azure").build();
        ParkingVehicle model = mapper.translate(request);
        assertEquals("NL-GY-894", model.getLicensePlateNumber());
        assertEquals("Azure", model.getStreetName());
        assertEquals(ParkingStatus.START.getParkingStatus(), model.getStatus());
    }
}
