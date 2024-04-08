package com.parking.nl.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutputResponse {

    private String message;

    private Status status;

    private String parkingFee;


}