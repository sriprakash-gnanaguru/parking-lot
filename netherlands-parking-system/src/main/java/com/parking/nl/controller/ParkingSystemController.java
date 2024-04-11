package com.parking.nl.controller;

import com.parking.nl.domain.request.ParkingRequest;
import com.parking.nl.domain.response.OutputResponse;
import com.parking.nl.domain.response.Status;
import com.parking.nl.exception.ErrorResponse;
import com.parking.nl.service.ParkingSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.parking.nl.common.Constants.PARKING_CONTROLLER_MSG;

@RestController
@RequestMapping(value = "/parking/v1")
@Slf4j
public class ParkingSystemController {
    private final ParkingSystemService parkingService;

    @Autowired
    public ParkingSystemController(ParkingSystemService parkingService) {
        this.parkingService = parkingService;
    }

    @Operation(summary = "Start the parking session of the vehicle", description = "This API is used to start the parking session of the vehicle.")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200", description="Success. Parking session has begun",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OutputResponse.class)) }),
            @ApiResponse(responseCode = "400", description="Bad Request. Invalid input provided",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description="Internal Server Error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping(value = "/vehicles/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OutputResponse> registerParking(@RequestBody @Valid ParkingRequest registerParkingRequest) {
        parkingService.registerParking(registerParkingRequest);
        log.info("Parking successfully registered for license Plate: {}", registerParkingRequest.getLicensePlateNumber());
        return ResponseEntity.ok().body(OutputResponse.builder().message(String.format(PARKING_CONTROLLER_MSG, registerParkingRequest.getLicensePlateNumber())).status(Status.SUCCESS).build());
    }

    @Operation(summary = "Stop the parking session of the vehicle", description = "This API is used to stop the active parking session of the vehicle.")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200", description="Success. Parking session has ended",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OutputResponse.class)) }),
            @ApiResponse(responseCode = "400", description="Bad Request. Invalid input provided",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description="Internal Server Error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PutMapping(value = "/vehicles/{vehicleNumber}/stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OutputResponse> unregisterParking(@Parameter(description = "Vehicle number", required = true)
                                                                @NotBlank @PathVariable(value = "vehicleNumber") String vehicleNumber) {
        OutputResponse response = parkingService.unregisterParking(vehicleNumber);
        log.info("Parking of the vehicle successfully ended for license Plate: {}", vehicleNumber);
        return ResponseEntity.ok().body(response);
    }
}

