package com.parking.nl.data.repository;

import com.parking.nl.data.model.ParkingStatus;
import com.parking.nl.data.model.ParkingVehicle;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class ParkingVehicleSpecificationTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ParkingVehiclesRepository repository;
    private ParkingVehicle vehicle;

    @BeforeEach
    public void setUp() {
        vehicle = ParkingVehicle.builder().licensePlateNumber("NL-UI-975").streetName("Java").startTime(LocalDateTime.now()).status(ParkingStatus.START.getParkingStatus()).build();
        entityManager.persistAndFlush(vehicle);
    }

    @Test
    @DisplayName("Postive Scenario:Custom Specification for Parking Vehicles retreives the data with license plate number for active parking ")
    public void testSpecification(){
        Specification<ParkingVehicle> spec = Specification.where(ParkingVehiclesSpecification.findVehicleByLicensePlateNumber("NL-UI-975"))
                .and(ParkingVehiclesSpecification.findVehicleByStatus(ParkingStatus.START.getParkingStatus()));
        List<ParkingVehicle> results = repository.findAll(spec);
        Assertions.assertThat(results).isNotEmpty();
        Assertions.assertThat(results).contains(this.vehicle);
    }

    @Test
    @DisplayName("Negative Scenario:Custom Specification for Parking Vehicles retreives no data if license plate no has no active parking  ")
    public void testSpecificationThenNoData() {
        Specification<ParkingVehicle> spec = Specification.where(ParkingVehiclesSpecification.findVehicleByLicensePlateNumber("NL-UI-098"))
                .and(ParkingVehiclesSpecification.findVehicleByStatus(ParkingStatus.START.getParkingStatus()));
        List<ParkingVehicle> results = repository.findAll(spec);
        Assertions.assertThat(results).isEmpty();
    }

}
