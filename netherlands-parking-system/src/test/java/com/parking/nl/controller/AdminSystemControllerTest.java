package com.parking.nl.controller;

import com.parking.nl.exception.InvalidInputException;
import com.parking.nl.service.AdminRegisterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@WebMvcTest(AdminRegisterController.class)
public class AdminSystemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminRegisterService service;

    @Test
    @DisplayName("Success/Happy flow scenario")
    public void whenPostWithValidRequest_thenCorrectResponse() throws Exception {
        doNothing().when(service).persistUnregisterVehicles(any());
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/v1/vehicles/penalty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"licensePlateNumber\":\"NL-69-YT\",\"streetName\":\"Azure\",\"checkInDateTime\":\"2024-04-05T07:34:55\"}," +
                                "{\"licensePlateNumber\":\"NL-07-EH\",\"streetName\":\"Oracle\",\"checkInDateTime\":\"2024-04-05T06:24:45\"}," +
                                "{\"licensePlateNumber\":\"NL-89-ZA\",\"streetName\":\"Java\",\"checkInDateTime\":\"2024-03-05T06:12:08\"}]"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Negative scenario with no request body")
    public void whenPostWithRuntimeException_thenInCorrectResponse() throws Exception {
        doThrow(new RuntimeException()).when(service).persistUnregisterVehicles(any());
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/v1/vehicles/penalty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"licensePlateNumber\":\"NL-69-YT\",\"streetName\":\"Torreslaan\",\"checkInDateTime\":\"2024-04-05T07:34:55\"}]"))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

}