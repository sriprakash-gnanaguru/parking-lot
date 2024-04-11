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
@Table(name = "parking_vehicle")
public class ParkingVehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parking_id")
    private Long parkingId;

    /**
     * Licence plate number
     * Unique with Licence Plate and end date null
     */
    @Column(name = "license_plate_number", nullable = false)
    private String licensePlateNumber;

    /**
     * Status of the parking session
     */
    @Column(name = "status")
    private String status;

    /**
     * Street name for the parking spot
     */
    /**
     * Street name for the parking spot
     */
    @Column(name = "street_name")
    private String streetName;


    /**
     * Payable amount for the parking
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * Start time of the parking
     */
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    /**
     * End time of the parking
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;


    @Column(name = "last_updated_time")
    private LocalDateTime lastUpdatedTime;

}
