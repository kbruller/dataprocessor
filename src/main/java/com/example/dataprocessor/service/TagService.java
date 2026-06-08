package com.example.dataprocessor.service;

import com.example.dataprocessor.entity.TagEntity;
import com.example.dataprocessor.exception.TagInUseException;
import com.example.dataprocessor.exception.TagNotFoundException;
import com.example.dataprocessor.mapper.TagMapper;
import com.example.dataprocessor.model.dto.TagRequest;
import com.example.dataprocessor.model.dto.TagResponse;
import com.example.dataprocessor.repository.DataTagRepository;
import com.example.dataprocessor.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final DataTagRepository dataTagRepository;
    private final TagMapper tagMapper;

    public TagService(
            TagRepository tagRepository,
            DataTagRepository dataTagRepository,
            TagMapper tagMapper
    ) {
        this.tagRepository = tagRepository;
        this.dataTagRepository = dataTagRepository;
        this.tagMapper = tagMapper;
    }

    @Transactional
    public TagResponse createTag(TagRequest request) {
        TagEntity entity = tagMapper.toEntity(request);

        TagEntity saved = tagRepository.save(entity);

        return tagMapper.toResponse(saved);
    }

    @Transactional
    public TagResponse updateTag(Long id, TagRequest request) {
        TagEntity entity = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));

        entity.setName(request.name());

        return tagMapper.toResponse(entity);
    }

//    @Transactional
//    public void deleteTag(Long id) {
//        TagEntity entity = tagRepository.findById(id)
//                .orElseThrow(() -> new TagNotFoundException(id));
//
//        tagRepository.delete(entity);
//    }

    @Transactional
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(tagMapper::toResponse)
                .toList();
    }

    @Transactional
    public TagResponse getTagById(Long id) {
        TagEntity entity = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));

        return tagMapper.toResponse(entity);
    }

    @Transactional
    public void deleteTag(Long id) {
        TagEntity entity = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));

        if (dataTagRepository.existsByTagId(id)) {
            throw new TagInUseException(id);
        }

        tagRepository.delete(entity);
    }
}