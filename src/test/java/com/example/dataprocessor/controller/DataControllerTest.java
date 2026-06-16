package com.example.dataprocessor.controller;

import com.example.dataprocessor.exception.DataNotFoundException;
import com.example.dataprocessor.model.dto.DataRequest;
import com.example.dataprocessor.model.dto.DataResponse;
import com.example.dataprocessor.service.DataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Test
    void shouldReturnPagedData() throws Exception {
        // 1. GIVEN - We prepare the list and wrap it in a PageImpl
        DataResponse data1 = new DataResponse(
                1L,
                "Temp-1",
                22.0,
                10L,
                "Sensors",
                Set.of());

        DataResponse data2 = new DataResponse(
                2L,
                "Temp-2",
                24.5,
                10L,
                "Sensors",
                Set.of());

        List<DataResponse> content = List.of(data1, data2);

        // We pass the list to PageImpl along with default pagination information (page 0, size 5)
        Page<DataResponse> pagedResponse = new PageImpl<>(
                content,
                PageRequest.of(0, 5),
                content.size());

        // Since the controller expects a `Pageable`, we use `any(Pageable.class)` for the mock
        when(dataService.search(any(), any(), any(), any(Pageable.class)))
                .thenReturn(pagedResponse);

        // 2. WHEN & THEN
        mockMvc.perform(get("/data")
                        .param("name", "Temp") // Let's simulate the RequestParams
                        .param("minValue", "20.0")
                        .param("maxValue", "30.0"))
                .andExpect(status().isOk())

                // With jsonPath, we use $.content to access the array!
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2)) // Check the quantity
                .andExpect(jsonPath("$.content[0].id").value(data1.id()))
                .andExpect(jsonPath("$.content[0].name").value(data1.name()))
                .andExpect(jsonPath("$.content[1].id").value(data2.id()))
                .andExpect(jsonPath("$.content[1].name").value(data2.name()))
                // We can also check the pagination fields automatically generated by Spring Data:
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.number").value(0)); // Current page number
    }

    @Test
    void shouldUpdateData() throws Exception {
        // GIVEN
        Long targetId = 1L;
        DataRequest request = new DataRequest(
                "Updated Name",
                30.0,
                10L,
                Set.of());

        DataResponse response = new DataResponse(
                targetId,
                "Updated Name",
                30.0,
                10L,
                "Sensors",
                Set.of());

        when(dataService.updateData(eq(targetId), any(DataRequest.class)))
                .thenReturn(response);

        // WHEN & THEN
        mockMvc.perform(put("/data/{id}", targetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(targetId))
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.value").value(request.value()));

        verify(dataService).updateData(eq(targetId), any(DataRequest.class));
    }

}