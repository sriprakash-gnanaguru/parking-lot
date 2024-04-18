package com.parking.nl.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "unregistered_vehicles_observation")
public class UnRegsiteredVehicles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "observation_id")
    private Long observationId;

    /**
     * Licence plate number
     * Unique with Licence Plate and end date null
     */
    @Column(name = "license_plate_number", nullable = false)
    private String licensePlateNumber;

    /**
     * Is penalty communicated?
     */
    @Column(name = "is_notified")
    private boolean isNotified;

    /**
     * Street name for the parking spot
     */
    /**
     * Street name for the parking spot
     */
    @Column(name = "street_name")
    private String streetName;

    /**
     * Start time of the parking
     */
    @Column(name = "observation_time", nullable = false)
    private LocalDateTime observationTime;

    /**
     * Payable amount for the parking
     */
    @Column(name = "price")
    private BigDecimal price;



    @Column(name = "last_updated_time")
    private LocalDateTime lastUpdatedTime;

}
