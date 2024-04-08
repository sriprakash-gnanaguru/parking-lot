package com.parking.nl.data.repository;

import com.parking.nl.data.model.ParkingVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingVehiclesRepository extends JpaRepository<ParkingVehicle,Long>, JpaSpecificationExecutor<ParkingVehicle> {

    Optional<ParkingVehicle> findByLicensePlateNumberAndStatus(String licensePlateNumber, String status);

}
