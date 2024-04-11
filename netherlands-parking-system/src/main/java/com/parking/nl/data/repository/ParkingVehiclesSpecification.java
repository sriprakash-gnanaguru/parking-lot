package com.parking.nl.data.repository;

import com.parking.nl.data.model.ParkingVehicle;
import org.springframework.data.jpa.domain.Specification;

import static com.parking.nl.common.Constants.LICENSE_PLATE_NUMBER;
import static com.parking.nl.common.Constants.STATUS;

public class ParkingVehiclesSpecification {

    public static Specification<ParkingVehicle> findVehicleByLicensePlateNumber(String licensePlateNumber) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(LICENSE_PLATE_NUMBER),  licensePlateNumber );
    }

    public static Specification<ParkingVehicle> findVehicleByStatus(String status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(STATUS),  status );
    }
}
