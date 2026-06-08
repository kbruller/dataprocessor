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

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/data")
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/sample")
    public DataResponse sample() {
        return new DataResponse(0L, "test", 42.0, 1L, "Test_category", Set.of("urgent", "weather"));
    }

    @PostMapping
    public DataResponse create(@Valid @RequestBody DataRequest request) {
        return dataService.createData(request);
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

//    @GetMapping
//    public Page<DataResponse> getData(
//            @RequestParam(required = false) Double minValue,
//            @RequestParam(required = false) Double maxValue,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size,
//            @RequestParam(defaultValue = "id") String sortBy,
//            @RequestParam(defaultValue = "asc") String sortDirection
//    ) {
//
//        return dataService.search(
//                minValue,
//                maxValue,
//                page,
//                size,
//                sortBy,
//                sortDirection
//        );
//    }

    @GetMapping
    public Page<DataResponse> getData(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minValue,
            @RequestParam(required = false) Double maxValue,
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return dataService.search(name, minValue, maxValue, pageable);
    }

//    @GetMapping
//    public List<DataResponse> getAll() {
//        return dataService.getAllData();
//    }

    @PostMapping("/search")
    public Page<DataResponse> searchData(
            @RequestBody DataSearchRequest request,
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "id",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        return dataService.search(request, pageable);
    }

    @GetMapping("/stats")
    public StatsResponse getStats() {
        return dataService.getStats();
    }

    @GetMapping("/{id}")
    public DataResponse getById(@PathVariable Long id) {
        return dataService.getById(id);
    }

}