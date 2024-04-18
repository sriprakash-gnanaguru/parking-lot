package com.parking.nl.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParkingMonitoringResponse {

    private String message;

    private Status status;

    private BigDecimal parkingFee;


}