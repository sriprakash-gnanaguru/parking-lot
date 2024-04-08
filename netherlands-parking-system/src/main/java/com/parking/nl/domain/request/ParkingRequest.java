package com.parking.nl.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParkingRequest {

    @NotBlank(message = "License plate number should not be empty")
    private String licensePlateNumber;

    @NotBlank(message = "Street Name should not be empty")
    private String streetName;

    @NotNull(message = "Check-in time should not be empty or null")
    private LocalDateTime checkInDateTime;
}
