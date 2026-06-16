package com.example.dataprocessor.controller;

import com.example.dataprocessor.exception.DataNotFoundException;
import com.example.dataprocessor.model.dto.DataRequest;
import com.example.dataprocessor.model.dto.DataResponse;
import com.example.dataprocessor.service.DataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; // <-- IMPORTÁLD BE

@WebMvcTest(DataController.class)
class DataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DataService dataService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnDataById() throws Exception {
        // Given
        DataResponse response = new DataResponse(
                1L,
                "Temperature",
                25.0,
                10L,
                "Sensors",
                Set.of("urgent")
        );

        when(dataService.getById(1L)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/data/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Temperature"))
                .andExpect(jsonPath("$.value").value(25.0))
                .andExpect(jsonPath("$.categoryId").value(10))
                .andExpect(jsonPath("$.categoryName").value("Sensors"))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags[0]").value("urgent"));
    }

    @Test
    void shouldCreateData() throws Exception {

        DataRequest request = new DataRequest(
                "Temperature",
                25.0,
                null,
                null
        );

        DataResponse response = new DataResponse(
                1L,
                "Temperature",
                25.0,
                null,
                null,
                Set.of()
        );

        when(dataService.createData(request)).thenReturn(response);

        mockMvc.perform(
                        post("/data")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Temperature"))
                .andExpect(jsonPath("$.value").value(25.0));

        // verify(dataService).createData(any(DataRequest.class));
        verify(dataService).createData(request);
    }

    @Test
    void shouldReturnBadRequestWhenCreateRequestIsInvalid() throws Exception {

        DataRequest request = new DataRequest(
                "",
                -5.0,
                null,
                null
        );

        mockMvc.perform(
                        post("/data")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                // .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                // Fields of the ApiErrorResponse
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                // Let's check whether the specific field errors are present in the map:
                .andExpect(jsonPath("$.validationErrors.name").exists())
                .andExpect(jsonPath("$.validationErrors.value").exists());

        verify(dataService, never())
                .createData(any(DataRequest.class));
    }

    @Test
    void shouldReturnValidationErrorResponseWhenCreateRequestIsInvalid() throws Exception {

        DataRequest request = new DataRequest(
                "",
                -5.0,
                null,
                null
        );

        mockMvc.perform(
                        post("/data")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.validationErrors.name").exists())
                .andExpect(jsonPath("$.validationErrors.value").exists());

        verify(dataService, never())
                .createData(any(DataRequest.class));
    }

    @Test
    void shouldReturnNotFoundWhenDataDoesNotExist() throws Exception {

        when(dataService.getById(999L))
                .thenThrow(new DataNotFoundException(999L));

        mockMvc.perform(get("/data/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Data not found with id: 999"));
    }


}