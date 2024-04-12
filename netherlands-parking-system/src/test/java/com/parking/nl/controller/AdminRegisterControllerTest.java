package com.parking.nl.controller;

import com.parking.nl.service.AdminRegisterService;
import net.joshka.junit.json.params.JsonFileSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.json.JsonObject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@SpringJUnitWebConfig
@ContextConfiguration
@WebMvcTest(controllers  = AdminRegisterController.class)
public class AdminRegisterControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private AdminRegisterService service;
    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @ParameterizedTest
    @JsonFileSource(resources = {"/json/Admin_Input_Request.json"})
    public void whenPostWithValidRequest_thenCorrectResponse(final JsonObject request) throws Exception {
        doNothing().when(service).persistUnregisterVehicles(any());
        String requestString = request.getJsonArray("InputRequestData").toString();
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/admin/v1/vehicles/penalty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString));
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

    }

    @ParameterizedTest
    @JsonFileSource(resources = {"/json/Admin_Input_Request.json"})
    public void whenPostWithRuntimeException_thenInCorrectResponse(final JsonObject request) throws Exception {
        doThrow(new RuntimeException()).when(service).persistUnregisterVehicles(any());
        String requestString = request.getJsonArray("InputRequestData").toString();
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/v1/vehicles/penalty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

}