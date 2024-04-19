package com.parking.nl.mapper;

import com.parking.nl.data.model.ParkingVehicle;
import com.parking.nl.data.model.Street;
import com.parking.nl.domain.request.ParkingRequest;
import com.parking.nl.domain.request.StreetRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StreetMapper {

    @Mapping(source = "streetName", target = "name")
    @Mapping(source = "price", target = "pricePerMinute")
    Street translate(StreetRequest parkingRequest);

}
