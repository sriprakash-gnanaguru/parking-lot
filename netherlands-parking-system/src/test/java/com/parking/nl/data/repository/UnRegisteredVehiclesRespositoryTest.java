package com.parking.nl.data.repository;

import com.parking.nl.data.model.UnRegsiteredVehicles;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class UnRegisteredVehiclesRespositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UnRegisteredVehiclesRepository repository;
    private UnRegsiteredVehicles vehicle;

    @BeforeEach
    public void setUp() {
        vehicle = UnRegsiteredVehicles.builder().licensePlateNumber("NL-TY-876").streetName("Java").observationTime(LocalDateTime.now()).isNotified(Boolean.FALSE).build();
        entityManager.persistAndFlush(vehicle);
    }

    @AfterEach
    public void afterEach() {
        entityManager.clear();
    }

    @Test
    @DisplayName("Positive Scenario:Fetch the vehicle with license plate number with active parking")
    public void testFindByisNotified(){
        List<UnRegsiteredVehicles> vehicles = repository.findByisNotified(Boolean.FALSE);
        Assertions.assertThat(vehicles).isNotEmpty();
        Assertions.assertThat(vehicles.get(0).getLicensePlateNumber().equals(this.vehicle.getLicensePlateNumber()));
    }

    @Test
    @DisplayName("Negative Scenario:Fetch the vehicle with license plate number with active parking")
    public void testFindByLicensePlateNumberAndStatusThenNoData(){
        List<UnRegsiteredVehicles> vehicles = repository.findByisNotified(Boolean.TRUE);
        Assertions.assertThat(vehicles).isEmpty();
    }

}
