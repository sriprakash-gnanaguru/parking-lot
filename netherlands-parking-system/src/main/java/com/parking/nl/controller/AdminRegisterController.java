package com.parking.nl.controller;

import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import com.parking.nl.domain.response.OutputResponse;
import com.parking.nl.domain.response.Status;
import com.parking.nl.exception.ErrorResponse;
import com.parking.nl.service.AdminRegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.parking.nl.common.Constants.ADMIN_CONTROLLER_MSG;

@RestController
@RequestMapping(value = "/admin/v1")
@Slf4j
public class AdminRegisterController {
   private final AdminRegisterService adminRegisterService;
    @Autowired
    public AdminRegisterController(AdminRegisterService adminRegisterService) {
        this.adminRegisterService = adminRegisterService;
    }

    @Operation(summary = "This API is to register the unregistered vehicles in the streets by City Admin")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200", description="Success. Persisted the unregistered vehicles in the system for penalty processing",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OutputResponse.class)) }),
            @ApiResponse(responseCode = "400", description="Bad Request. Invalid input provided",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description="Internal Server Error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping(value = "/vehicles/penalty", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OutputResponse> persistUnregisteredVehicles(@RequestBody @Valid List<UnregisteredVehiclesRequest> unregisteredVehicles) {
        adminRegisterService.persistUnregisterVehicles(unregisteredVehicles);
        log.info("Registered the unregistered vehicles in the system for penalty processing");
        return ResponseEntity.ok().body(OutputResponse.builder().message(ADMIN_CONTROLLER_MSG).status(Status.SUCCESS).build());
    }

}
