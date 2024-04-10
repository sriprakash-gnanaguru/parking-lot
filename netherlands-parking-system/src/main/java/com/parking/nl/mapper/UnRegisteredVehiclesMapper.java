package com.parking.nl.mapper;

import com.parking.nl.data.model.ParkingVehicle;
import com.parking.nl.data.model.UnRegsiteredVehicles;
import com.parking.nl.domain.request.ParkingRequest;
import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UnRegisteredVehiclesMapper {
    @Mapping(source = "licensePlateNumber", target = "licensePlateNumber")
    @Mapping(source = "streetName", target = "streetName")
    @Mapping(target = "isNotified", expression = "java(Boolean.FALSE)")
    @Mapping(source = "checkInDateTime", target = "observationTime")
    @Mapping(target = "lastUpdatedTime", expression = "java(java.time.LocalDateTime.now())")
    UnRegsiteredVehicles translate(UnregisteredVehiclesRequest request);

}
