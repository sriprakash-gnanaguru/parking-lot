package com.parking.nl.domain.request;

import com.parking.nl.validator.NotNullConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreetRequest {

    @NotNull
    private BigDecimal price;

    @NotNullConstraint
    private String streetName;

}
