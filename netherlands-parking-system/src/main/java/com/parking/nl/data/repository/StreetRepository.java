package com.parking.nl.data.repository;

import com.parking.nl.data.model.Street;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StreetRepository extends JpaRepository<Street,Long> {

    Optional<Street> findByNameIgnoreCase(String streetName);
}
