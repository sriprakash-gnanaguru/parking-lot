package com.parking.nl.controller;

import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import com.parking.nl.domain.response.ParkingMonitoringResponse;
import com.parking.nl.domain.response.Status;
import com.parking.nl.exception.ErrorResponse;
import com.parking.nl.scheduler.ReportScheduler;
import com.parking.nl.service.AdminRegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.parking.nl.common.Constants.*;

@RestController
@RequestMapping(value = "/admin/v1")
@Slf4j
public class AdminRegisterController {
   private final AdminRegisterService adminRegisterService;
    private final ReportScheduler scheduler;
   /* @Autowired
    public AdminRegisterController(AdminRegisterService adminRegisterService) {
        this.adminRegisterService = adminRegisterService;
    }*/


    @Autowired
    public AdminRegisterController(AdminRegisterService adminRegisterService, ReportScheduler scheduler) {
        this.adminRegisterService = adminRegisterService;
        this.scheduler = scheduler;
    }

    @Operation(summary = "This API is to register the unregistered vehicles in the streets by City Admin")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200", description="Success. Persisted the unregistered vehicles in the system for penalty processing",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ParkingMonitoringResponse.class)) }),
            @ApiResponse(responseCode = "400", description="Bad Request. Invalid input provided",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description="Internal Server Error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping(value = "/vehicles/monitor")
    public ResponseEntity<ParkingMonitoringResponse> persistUnregisteredVehicles(@RequestBody @Valid List<UnregisteredVehiclesRequest> unregisteredVehicles) {
        List<String> output = adminRegisterService.persistUnregisterVehicles(unregisteredVehicles);
        StringBuilder builder = new StringBuilder();
        builder.append(ADMIN_CONTROLLER_MSG);
        builder.append(output.toString().replace(OPEN_BRACKET,EMPTY).replace(CLOSE_BRACKET,EMPTY));
        scheduler.perform();
        log.info("Registered the unregistered vehicles in the system for penalty processing");
        return ResponseEntity.ok().body(ParkingMonitoringResponse.builder().message(builder.toString()).status(Status.SUCCESS).build());
    }

}
