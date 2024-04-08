package com.parking.nl.mapper;

import com.parking.nl.data.model.ParkingVehicle;
import com.parking.nl.domain.request.ParkingRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParkingVehiclesMapper {
    @Mapping(source = "licensePlateNumber", target = "licensePlateNumber")
    @Mapping(source = "streetName", target = "streetName")
    @Mapping(target = "status", expression = "java(com.parking.nl.data.model.ParkingStatus.START.getParkingStatus())")
    @Mapping(source = "checkInDateTime", target = "startTime")
    @Mapping(target = "lastUpdatedTime", expression = "java(java.time.LocalDateTime.now())")
    ParkingVehicle translate(ParkingRequest parkingRequest);

}
