package com.parking.nl.data.repository;

import com.parking.nl.data.model.ParkingVehicle;
import com.parking.nl.data.model.UnRegsiteredVehicles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnRegisteredVehiclesRepository extends JpaRepository<UnRegsiteredVehicles,Long>, JpaSpecificationExecutor<ParkingVehicle> {

    List<UnRegsiteredVehicles> findByisNotified(Boolean flg);

}
