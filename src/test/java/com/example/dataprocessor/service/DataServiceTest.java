package com.example.dataprocessor.service;

import com.example.dataprocessor.entity.CategoryEntity;
import com.example.dataprocessor.entity.DataEntity;
import com.example.dataprocessor.entity.TagEntity;
import com.example.dataprocessor.exception.CategoryNotFoundException;
import com.example.dataprocessor.exception.DataNotFoundException;
import com.example.dataprocessor.exception.TagNotFoundException;
import com.example.dataprocessor.mapper.DataMapper;
import com.example.dataprocessor.model.dto.DataRequest;
import com.example.dataprocessor.model.dto.DataResponse;
import com.example.dataprocessor.repository.CategoryRepository;
import com.example.dataprocessor.repository.DataRepository;
import com.example.dataprocessor.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DataServiceTest {

    @Mock
    private DataRepository dataRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private DataMapper dataMapper;

    @InjectMocks
    private DataService dataService;

    @Test
    void getByIdShouldReturnDataWhenEntityExists() {
        DataEntity entity =  DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();

        DataResponse response = new DataResponse(
                1L,
                "Temperature",
                25.0,
                null,
                null,
                Set.of()
        );

        when(dataRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        when(dataMapper.toResponse(entity))
                .thenReturn(response);

        DataResponse result = dataService.getById(1L);

        assertThat(result).isEqualTo(response);

        verify(dataRepository).findById(1L);
        verify(dataMapper).toResponse(entity);
    }

    @Test
    void getByIdShouldThrowExceptionWhenEntityDoesNotExist() {
        when(dataRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                DataNotFoundException.class,
                () -> dataService.getById(1L)
        );

        verify(dataRepository).findById(1L);
        verifyNoInteractions(dataMapper);
    }

    @Test
    void createDataShouldSaveEntity() {

        DataRequest request = new DataRequest(
                "Temperature",
                25.0,
                null,
                null
        );

        DataEntity entity =  DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();

        DataEntity savedEntity =  DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();
        savedEntity.setId(1L);

        DataResponse response = new DataResponse(
                1L,
                "Temperature",
                25.0,
                null,
                null,
                Set.of()
        );

        when(dataMapper.toEntity(request))
                .thenReturn(entity);

        when(dataRepository.save(entity))
                .thenReturn(savedEntity);

        when(dataMapper.toResponse(savedEntity))
                .thenReturn(response);

        DataResponse result =
                dataService.createData(request);

        assertThat(result).isEqualTo(response);

        verify(dataMapper).toEntity(request);
        verify(dataRepository).save(entity);
        verify(dataMapper).toResponse(savedEntity);
    }

    @Test
    void createDataShouldAssignCategoryWhenCategoryIdIsProvided() {
        DataRequest request = new DataRequest(
                "Temperature",
                25.0,
                10L,
                null
        );

        DataEntity entity =  DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();

        CategoryEntity category = CategoryEntity.builder()
                .id(10L)
                .name("Sensors")
                .build();

        DataEntity savedEntity =  DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();
        savedEntity.setId(1L);
        savedEntity.setCategory(category);

        DataResponse response = new DataResponse(
                1L,
                "Temperature",
                25.0,
                10L,
                "Sensors",
                Set.of()
        );

        when(dataMapper.toEntity(request)).thenReturn(entity);
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(dataRepository.save(any(DataEntity.class))).thenReturn(savedEntity);
        when(dataMapper.toResponse(savedEntity)).thenReturn(response);

        dataService.createData(request);

        ArgumentCaptor<DataEntity> captor =
                ArgumentCaptor.forClass(DataEntity.class);

        verify(dataRepository).save(captor.capture());

        DataEntity capturedEntity = captor.getValue();

        assertThat(capturedEntity.getCategory()).isEqualTo(category);
//        assertThat(capturedEntity.getCategory().getId()).isEqualTo(10L);
//        assertThat(capturedEntity.getCategory().getName()).isEqualTo("Sensors");
    }


    @Test
    void createDataShouldAssignCategoryWhenCategoryIdIsProvided2() {
        DataRequest request = new DataRequest(
                "Temperature",
                25.0,
                10L,
                null
        );

        DataEntity entity =  DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();

        CategoryEntity category = CategoryEntity.builder()
                .id(10L)
                .name("Sensors")
                .build();

        DataEntity savedEntity =  DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();
        savedEntity.setId(1L);
        savedEntity.setCategory(category);

        DataResponse response = new DataResponse(
                1L,
                "Temperature",
                25.0,
                10L,
                "Sensors",
                Set.of()
        );

        when(dataMapper.toEntity(request)).thenReturn(entity);
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(dataMapper.toResponse(savedEntity)).thenReturn(response);
        // In when phase instead of any() we have a condition:
        when(dataRepository.save(argThat(ent -> ent.getCategory() != null
                && ent.getCategory().getId().equals(10L)))).thenReturn(savedEntity);

        dataService.createData(request);

        verify(dataRepository).save(entity);
        assertThat(entity.getCategory()).isEqualTo(category);
    }

    @Test
    void createDataShouldNotLoadCategoryWhenCategoryIdIsNull() {

        DataRequest request = new DataRequest(
                "Temperature",
                25.0,
                null,
                null
        );

        DataEntity entity = DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();

        DataEntity savedEntity =  DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();
        savedEntity.setId(1L);

        DataResponse response = new DataResponse(
                1L,
                "Temperature",
                25.0,
                null,
                null,
                Set.of()
        );

        when(dataMapper.toEntity(request))
                .thenReturn(entity);

        when(dataRepository.save(any(DataEntity.class)))
                .thenReturn(savedEntity);

        when(dataMapper.toResponse(savedEntity))
                .thenReturn(response);

        dataService.createData(request);

        verify(categoryRepository, never())
                .findById(anyLong());

        verify(dataRepository)
                .save(any(DataEntity.class));
    }

    @Test
    void createDataShouldLoadTagsWhenTagIdsAreProvided() {

        DataRequest request = new DataRequest(
                "Temperature",
                25.0,
                null,
                Set.of(1L, 2L)
        );


        DataEntity entity = DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();

        TagEntity tag1 = TagEntity.builder()
                .id(1L)
                .name("urgent")
                .build();

        TagEntity tag2 = TagEntity.builder()
                .id(2L)
                .name("weather")
                .build();

        DataEntity savedEntity =  DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();
        savedEntity.setId(1L);

        DataResponse response = new DataResponse(
                1L,
                "Temperature",
                25.0,
                null,
                null,
                Set.of("urgent", "weather")
        );

        when(dataMapper.toEntity(request))
                .thenReturn(entity);

        when(tagRepository.findById(1L))
                .thenReturn(Optional.of(tag1));

        when(tagRepository.findById(2L))
                .thenReturn(Optional.of(tag2));

        when(dataRepository.save(any(DataEntity.class)))
                .thenReturn(savedEntity);

        when(dataMapper.toResponse(savedEntity))
                .thenReturn(response);

        DataResponse result = dataService.createData(request);


        assertThat(result).isEqualTo(response);

        verify(tagRepository).findById(1L);
        verify(tagRepository).findById(2L);

        verify(dataRepository).save(any(DataEntity.class));

        verifyNoMoreInteractions(tagRepository);
    }

    @Test
    void createDataShouldCallFindByIdExactlyAsManyTimesAsTagsProvided() {
        Set<Long> inputTagIds = Set.of(10L, 20L, 30L);
        DataRequest request = new DataRequest("SensorData", 42.0, null, inputTagIds);

        DataEntity entity = DataEntity.builder()
                .name("SensorData")
                .value(42.0)
                .build();

        DataEntity savedEntity = DataEntity.builder()
                .id(1L)
                .name("SensorData")
                .value(42.0)
                .build();

        DataResponse response = new DataResponse(1L, "SensorData", 42.0, null, null, Set.of());

        when(dataMapper.toEntity(request)).thenReturn(entity);
        when(dataRepository.save(any(DataEntity.class))).thenReturn(savedEntity);
        when(dataMapper.toResponse(savedEntity)).thenReturn(response);

        when(tagRepository.findById(anyLong()))
                .thenReturn(Optional.of(TagEntity.builder().name("Dummy").build()));

        dataService.createData(request);

        verify(tagRepository, times(3)).findById(anyLong());

        verify(dataRepository, times(1)).save(any(DataEntity.class));
    }

    @Test
    void createDataShouldThrowExceptionWhenTagDoesNotExist() {

        DataRequest request = new DataRequest(
                "Temperature",
                25.0,
                null,
                Set.of(1L)
        );

        DataEntity entity =  DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();

        when(dataMapper.toEntity(request))
                .thenReturn(entity);

        when(tagRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                TagNotFoundException.class,
                () -> dataService.createData(request)
        );

        verify(tagRepository).findById(1L);

        verify(dataRepository, never())
                .save(any(DataEntity.class));

        verify(dataMapper, never())
                .toResponse(any(DataEntity.class));
    }

    @Test
    void updateDataShouldUpdateEntityFieldsCategoryAndTags() {
        DataRequest request = new DataRequest(
                "Temperature Updated",
                30.0,
                10L,
                Set.of(1L)
        );

        DataEntity entity = DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();

        CategoryEntity category = CategoryEntity.builder()
                .id(10L)
                .name("Sensors")
                .build();

        TagEntity tag = TagEntity.builder()
                .id(1L)
                .name("urgent")
                .build();

        DataResponse response = new DataResponse(
                1L,
                "Temperature Updated",
                30.0,
                10L,
                "Sensors",
                Set.of("urgent")
        );

        when(dataRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        when(categoryRepository.findById(10L))
                .thenReturn(Optional.of(category));

        when(tagRepository.findById(1L))
                .thenReturn(Optional.of(tag));

        when(dataMapper.toResponse(entity))
                .thenReturn(response);

        DataResponse result = dataService.updateData(1L, request);

        assertThat(result).isEqualTo(response);

        assertThat(entity.getName()).isEqualTo("Temperature Updated");
        assertThat(entity.getValue()).isEqualTo(30.0);
        assertThat(entity.getCategory()).isEqualTo(category);
        assertThat(entity.getDataTags()).hasSize(1);

        verify(dataRepository).findById(1L);
        verify(categoryRepository).findById(10L);
        verify(tagRepository).findById(1L);
        verify(dataMapper).toResponse(entity);

        verify(dataRepository, never()).save(any(DataEntity.class));
    }

    @Test
    void updateDataShouldThrowExceptionWhenDataDoesNotExist() {
        DataRequest request = new DataRequest(
                "Temperature Updated",
                30.0,
                10L,
                Set.of(1L)
        );

        when(dataRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                DataNotFoundException.class,
                () -> dataService.updateData(1L, request)
        );

        verify(dataRepository).findById(1L);

        verifyNoInteractions(categoryRepository);
        verifyNoInteractions(tagRepository);
        verifyNoInteractions(dataMapper);
    }


    @Test
    void updateDataShouldThrowExceptionWhenCategoryDoesNotExist() {
        DataRequest request = new DataRequest(
                "Temperature Updated",
                30.0,
                10L,
                Set.of(1L)
        );

        DataEntity entity =  DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();

        when(dataRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        when(categoryRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> dataService.updateData(1L, request)
        );

        verify(dataRepository).findById(1L);
        verify(categoryRepository).findById(10L);

        verifyNoInteractions(tagRepository);
        verify(dataMapper, never()).toResponse(any(DataEntity.class));
        verify(dataRepository, never()).save(any(DataEntity.class));
    }

    @Test
    void updateDataShouldThrowExceptionWhenTagDoesNotExist() {
        DataRequest request = new DataRequest(
                "Temperature Updated",
                30.0,
                10L,
                Set.of(1L)
        );

        DataEntity entity = DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();

        CategoryEntity category = CategoryEntity.builder()
                .id(10L)
                .name("Sensors")
                .build();

        when(dataRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        when(categoryRepository.findById(10L))
                .thenReturn(Optional.of(category));

        when(tagRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                TagNotFoundException.class,
                () -> dataService.updateData(1L, request)
        );

        verify(dataRepository).findById(1L);
        verify(categoryRepository).findById(10L);
        verify(tagRepository).findById(1L);

        verify(dataMapper, never()).toResponse(any(DataEntity.class));
        verify(dataRepository, never()).save(any(DataEntity.class));
    }
}
