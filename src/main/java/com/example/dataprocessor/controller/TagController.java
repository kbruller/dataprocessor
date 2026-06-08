package com.example.dataprocessor.controller;

import com.example.dataprocessor.model.dto.TagRequest;
import com.example.dataprocessor.model.dto.TagResponse;
import com.example.dataprocessor.service.TagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponse createTag(
            @Valid @RequestBody TagRequest request
    ) {
        return tagService.createTag(request);
    }

    @GetMapping
    public List<TagResponse> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public TagResponse getTagById(@PathVariable Long id) {
        return tagService.getTagById(id);
    }

    @PutMapping("/{id}")
    public TagResponse updateTag(
            @PathVariable Long id,
            @Valid @RequestBody TagRequest request
    ) {
        return tagService.updateTag(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
    }
}