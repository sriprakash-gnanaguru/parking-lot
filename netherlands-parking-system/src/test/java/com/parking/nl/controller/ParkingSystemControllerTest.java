package com.parking.nl.controller;

import com.parking.nl.data.model.ParkingStatus;
import com.parking.nl.data.model.ParkingVehicle;
import com.parking.nl.domain.request.ParkingRequest;
import com.parking.nl.domain.response.ParkingMonitoringResponse;
import com.parking.nl.exception.InvalidInputException;
import com.parking.nl.service.ParkingSystemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(ParkingSystemController.class)
public class ParkingSystemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ParkingSystemService service;


    @ParameterizedTest
    @ValueSource(strings = "{\"licensePlateNumber\":\"NL-69-YT\",\"streetName\":\"Azure\",\"checkInDateTime\":\"2024-04-05T07:34:55\"}")
    public void whenPostWithValidRequest_thenCorrectResponse(String ProperRequest) throws Exception {
        doNothing().when(service).registerParking(any(ParkingRequest.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/parking/v1/vehicles/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ProperRequest))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Negative scenario with no request body")
    public void whenPostWithRuntimeException_thenInCorrectResponse() throws Exception {
        doThrow(new RuntimeException()).when(service).registerParking(any(ParkingRequest.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/parking/v1/vehicles/start")
                        .contentType(MediaType.APPLICATION_JSON)
                       .content("") )
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

    @ParameterizedTest
    @DisplayName("Bad Request scenario")
    @ValueSource(strings = "{ \"licensePlateNumber\": \"\", \"streetName\": \"Java\", \"checkInDateTime\": \"2024-04-05T07:34:55\"}")
    public void whenPostWithBadRequest_thenInCorrectResponse(String inCorrectRequest) throws Exception {
        doThrow(new InvalidInputException("License plate number is empty")).when(service).registerParking(any(ParkingRequest.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/parking/v1/vehicles/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inCorrectRequest) )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @DisplayName("Success/Happy flow scenario")
    public void whenPutWithValidRequest_thenCorrectResponse() throws Exception {
        doReturn(ParkingMonitoringResponse.builder().parkingFee(BigDecimal.TEN).message("Success").build()).when(service).unregisterParking("NL-69-YT");
        mockMvc.perform(MockMvcRequestBuilders.put("/parking/v1/vehicles/NL-69-YT/stop")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Negative scenario")
    public void whenPutWithNoActiveSession_thenInCorrectResponse() throws Exception {
        doThrow(new InvalidInputException("No active parking session found for vehicle: NL-69-YT"));
        mockMvc.perform(MockMvcRequestBuilders.put("/parking/v1/vehicles/NL-69-YT/stop")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

}