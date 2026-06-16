package com.example.dataprocessor.controller;

import com.example.dataprocessor.model.dto.DataRequest;
import com.example.dataprocessor.model.dto.DataResponse;
import com.example.dataprocessor.model.dto.DataSearchRequest;
import com.example.dataprocessor.service.DataService;
import com.example.dataprocessor.model.dto.StatsResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/data", produces = APPLICATION_JSON_VALUE)
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse create(@Valid @RequestBody DataRequest request) {
        return dataService.createData(request);
    }

    @GetMapping
    public Page<DataResponse> getData(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minValue,
            @RequestParam(required = false) Double maxValue,
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return dataService.search(name, minValue, maxValue, pageable);
    }

    @GetMapping("/{id}")
    public DataResponse getById(@PathVariable Long id) {
        return dataService.getById(id);
    }

    @PostMapping("/search")
    public Page<DataResponse> searchData(
            @RequestBody DataSearchRequest request,
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return dataService.search(request, pageable);
    }

    @GetMapping("/stats")
    public StatsResponse getStats() {
        return dataService.getStats();
    }

    @PutMapping("/{id}")
    public DataResponse update(
            @PathVariable Long id,
            @Valid @RequestBody DataRequest request
    ) {
        return dataService.updateData(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        dataService.deleteData(id);
    }

    @GetMapping("/sample")
    public DataResponse sample() {
        return new DataResponse(0L, "test", 42.0, 1L, "Test_category", java.util.Set.of("urgent", "weather"));
    }
}