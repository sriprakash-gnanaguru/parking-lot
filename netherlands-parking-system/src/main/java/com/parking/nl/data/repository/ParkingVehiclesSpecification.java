package com.parking.nl.data.repository;

import com.parking.nl.data.model.ParkingVehicle;
import org.springframework.data.jpa.domain.Specification;

public class ParkingVehiclesSpecification {

    public static Specification<ParkingVehicle> findVehicleByLicensePlateNumber(String licensePlateNumber) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("licensePlateNumber"),  licensePlateNumber );
    }

    public static Specification<ParkingVehicle> findVehicleByStatus(String status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("status"),  status );
    }
}
