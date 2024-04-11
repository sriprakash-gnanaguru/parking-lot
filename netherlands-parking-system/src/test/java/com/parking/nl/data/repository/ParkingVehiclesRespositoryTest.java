package com.parking.nl.data.repository;

import com.parking.nl.data.model.ParkingStatus;
import com.parking.nl.data.model.ParkingVehicle;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
public class ParkingVehiclesRespositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ParkingVehiclesRepository repository;
    private ParkingVehicle vehicle;

    @BeforeEach
    public void setUp() {
        vehicle = ParkingVehicle.builder().licensePlateNumber("NL-TY-876").streetName("Java").startTime(LocalDateTime.now()).status(ParkingStatus.START.getParkingStatus()).build();
        entityManager.persistAndFlush(vehicle);
    }

    @AfterEach
    public void afterEach() {
        entityManager.clear();
    }

    @Test
    @DisplayName("Positive Scenario:Fetch the vehicle with license plate number with active parking")
    public void testFindByLicensePlateNumberAndStatus(){
        Optional<ParkingVehicle>  vehicle = repository.findByLicensePlateNumberAndStatus("NL-TY-876",ParkingStatus.START.getParkingStatus());
        Assertions.assertThat(vehicle).isPresent();
        Assertions.assertThat(vehicle.get().getLicensePlateNumber().equals(this.vehicle.getLicensePlateNumber()));
    }

    @Test
    @DisplayName("Negative Scenario:Fetch the vehicle with license plate number with active parking")
    public void testFindByLicensePlateNumberAndStatusThenNoData(){
        Optional<ParkingVehicle>  vehicle = repository.findByLicensePlateNumberAndStatus("NL-OU-876",ParkingStatus.START.getParkingStatus());
        Assertions.assertThat(vehicle).isEmpty();
    }

}
