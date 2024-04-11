package com.parking.nl.controller;

import com.parking.nl.data.model.ParkingStatus;
import com.parking.nl.data.model.ParkingVehicle;
import com.parking.nl.domain.request.ParkingRequest;
import com.parking.nl.exception.InvalidInputException;
import com.parking.nl.service.ParkingSystemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@WebMvcTest(ParkingSystemController.class)
public class ParkingSystemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ParkingSystemService service;
    private String ProperRequest;
    private String InCorrectRequest;

    @BeforeEach
    public void setUp() {
        ProperRequest = "{\"licensePlateNumber\":\"NL-69-YT\",\"streetName\":\"Azure\",\"checkInDateTime\":\"2024-04-05T07:34:55\"}";
        InCorrectRequest = "{ \"licensePlateNumber\": \"\", \"streetName\": \"Java\", \"checkInDateTime\": \"2024-04-05T07:34:55\"}";
    }

    @Test
    @DisplayName("Success/Happy flow scenario")
    public void whenPostWithValidRequest_thenCorrectResponse() throws Exception {
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

    @Test
    @DisplayName("Bad Request scenario")
    public void whenPostWithBadRequest_thenInCorrectResponse() throws Exception {
        doThrow(new InvalidInputException("License plate number is empty")).when(service).registerParking(any(ParkingRequest.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/parking/v1/vehicles/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(InCorrectRequest) )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @DisplayName("Success/Happy flow scenario")
    public void whenPutWithValidRequest_thenCorrectResponse() throws Exception {
        doNothing().when(service).registerParking(any(ParkingRequest.class));
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