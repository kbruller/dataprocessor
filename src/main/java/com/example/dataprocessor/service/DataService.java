package com.example.dataprocessor.service;

import com.example.dataprocessor.entity.CategoryEntity;
import com.example.dataprocessor.entity.DataEntity;
import com.example.dataprocessor.entity.TagEntity;
import com.example.dataprocessor.exception.*;
import com.example.dataprocessor.mapper.DataMapper;
import com.example.dataprocessor.model.dto.DataRequest;
import com.example.dataprocessor.model.dto.DataResponse;
import com.example.dataprocessor.model.dto.DataSearchRequest;
import com.example.dataprocessor.model.dto.StatsResponse;
import com.example.dataprocessor.repository.CategoryRepository;
import com.example.dataprocessor.repository.DataRepository;
import com.example.dataprocessor.repository.TagRepository;
import com.example.dataprocessor.specification.DataSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DataService {

    private final DataRepository dataRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final DataMapper dataMapper;

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("id", "name", "value");
    private static final int MAX_PAGE_SIZE = 100;

    public DataService(DataRepository dataRepository,
                       CategoryRepository categoryRepository, TagRepository tagRepository,
                       DataMapper dataMapper) {
        this.dataRepository = dataRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.dataMapper = dataMapper;
    }


    @Transactional
    public DataResponse createData(DataRequest request) {

        DataEntity entity = dataMapper.toEntity(request);

        if (request.categoryId() != null) {
            CategoryEntity category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));

            entity.setCategory(category);
        }

        if (request.tagIds() != null && !request.tagIds().isEmpty()) {
            request.tagIds().forEach(tagId -> {
                TagEntity tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new TagNotFoundException(tagId));

                entity.addTag(tag, "MANUAL");
            });
        }

        DataEntity saved = dataRepository.save(entity);

        return dataMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<DataResponse> getAllData() {
        return dataRepository.findAll()
                .stream()
                .map(dataMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DataResponse getById(Long id) {
        DataEntity entity = dataRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(id));
        return dataMapper.toResponse(entity);
    }

    @Transactional
    public DataResponse updateData(Long id, DataRequest request) {
        DataEntity entity = dataRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(id));

        System.out.println(
                entity.getCategory().getClass());

        entity.setName(request.name());
        entity.setValue(request.value());

        updateCategory(entity, request.categoryId());
        updateTags(entity, request.tagIds());

        return dataMapper.toResponse(entity);
    }

    private void updateCategory(DataEntity entity, Long categoryId) {
        if (categoryId == null) {
            entity.setCategory(null);
            return;
        }

        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        entity.setCategory(category);
    }

    private void updateTags(DataEntity entity, Set<Long> tagIds) {
        entity.getDataTags().clear();

        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }

        tagIds.forEach(tagId -> {
            TagEntity tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new TagNotFoundException(tagId));

            entity.addTag(tag, "MANUAL");
        });
    }

    @Transactional
    public void deleteData(Long id) {
        DataEntity entity = dataRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(id));

        dataRepository.delete(entity);
    }




//    @Transactional(readOnly = true)
//    public List<DataResponse> getByValueBetween(double minValue, double maxValue) {
//        return dataRepository.findByValueBetween(minValue, maxValue)
//                .stream()
//                .map(dataMapper::toResponse)
//                .toList();
//    }

//    @Transactional(readOnly = true)
//    public Page<DataResponse> search(
//            Double minValue,
//            Double maxValue,
//            int page,
//            int size,
//            String sortBy,
//            String sortDirection
//    ) {
//        Sort sort = sortDirection.equalsIgnoreCase("desc")
//                ? Sort.by(sortBy).descending()
//                : Sort.by(sortBy).ascending();
//
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        Page<DataEntity> entities;
//
//        if (minValue != null && maxValue != null) {
//            entities = dataRepository.findByValueBetween(
//                    minValue,
//                    maxValue,
//                    pageable
//            );
//        } else if (minValue != null) {
//            entities = dataRepository.findByValueGreaterThanEqual(
//                    minValue,
//                    pageable
//            );
//        } else {
//            entities = dataRepository.findAll(pageable);
//        }
//
//        return entities.map(dataMapper::toResponse);
//    }


    @Transactional(readOnly = true)
    public Page<DataResponse> search(
            String name,
            Double minValue,
            Double maxValue,
            Pageable pageable
    ) {
        validatePageable(pageable);

        Specification<DataEntity> spec =
                DataSpecification.withFilters(name, minValue, maxValue);

        return dataRepository.findAll(spec, pageable)
                .map(dataMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<DataResponse> search(
            DataSearchRequest request,
            Pageable pageable
    ) {
        return search(
                request.name(),
                request.minValue(),
                request.maxValue(),
                pageable
        );
    }

    @Transactional(readOnly = true)
    public StatsResponse getStats() {
        List<DataEntity> entities = dataRepository.findAll();

        if (entities.isEmpty()) {
            return new StatsResponse(0, 0, 0, 0);
        }

        DoubleSummaryStatistics stats = entities.stream()
                .mapToDouble(DataEntity::getValue)
                .summaryStatistics();

        return new StatsResponse(
                (int) stats.getCount(),
                stats.getMin(),
                stats.getMax(),
                stats.getAverage()
        );
    }

    private void validatePageable(Pageable pageable) {
        if (pageable.getPageSize() > MAX_PAGE_SIZE) {
            throw new InvalidPageSizeException( MAX_PAGE_SIZE );
        }

        validateSort(pageable);
    }
    private void validateSort(Pageable pageable) {
        for (Sort.Order order : pageable.getSort()) {
            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                throw new InvalidSortFieldException(order.getProperty());
            }
        }
    }

}