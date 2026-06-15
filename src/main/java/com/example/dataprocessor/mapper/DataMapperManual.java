package com.example.dataprocessor.mapper;

import com.example.dataprocessor.entity.DataEntity;
import com.example.dataprocessor.model.dto.DataRequest;
import com.example.dataprocessor.model.dto.DataResponse;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DataMapperManual {

    public DataEntity toEntity(DataRequest request) {
        return DataEntity.builder()
                .name(request.name())
                .value(request.value())
                .build();
    }

    public DataResponse toResponse(DataEntity entity) {

        Long categoryId = null;
        String categoryName = null;

        if (entity.getCategory() != null) {
            categoryId = entity.getCategory().getId();
            categoryName = entity.getCategory().getName();
        }

        Set<String> tags = entity.getDataTags()
                .stream()
                .map(dataTag -> dataTag.getTag().getName())
                .collect(Collectors.toSet());

        return new DataResponse(
                entity.getId(),
                entity.getName(),
                entity.getValue(),
                categoryId,
                categoryName,
                tags
        );
    }
}