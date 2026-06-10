package com.example.dataprocessor.mapper;

import com.example.dataprocessor.entity.DataEntity;
import com.example.dataprocessor.entity.DataTagEntity;
import com.example.dataprocessor.model.dto.DataRequest;
import com.example.dataprocessor.model.dto.DataResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DataMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "dataTags", ignore = true)
    DataEntity toEntity(DataRequest request);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "dataTags", target = "tags")
    DataResponse toResponse(DataEntity entity);

    default Set<String> mapDataTagsToTagNames(Set<DataTagEntity> dataTags) {
        if (dataTags == null) {
            return Set.of();
        }

        return dataTags.stream()
                .map(dataTag -> dataTag.getTag().getName())
                .collect(Collectors.toSet());
    }
}
