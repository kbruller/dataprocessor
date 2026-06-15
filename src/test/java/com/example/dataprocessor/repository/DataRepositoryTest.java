package com.example.dataprocessor.repository;

import com.example.dataprocessor.entity.CategoryEntity;
import com.example.dataprocessor.entity.DataEntity;
import com.example.dataprocessor.entity.DataTagEntity;
import com.example.dataprocessor.entity.TagEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DataRepositoryTest extends BaseJpaIntegrationTest {

    @Test
    void shouldSaveAndFindDataEntity() {
        DataEntity savedData = createAndSaveData("Temperature", 25.0);

        flushAndClear();

        assertThat(savedData.getId()).isNotNull();

        DataEntity found = dataRepository.findById(savedData.getId())
                .orElseThrow();

        assertThat(found.getName()).isEqualTo("Temperature");
        assertThat(found.getValue()).isEqualTo(25.0);
    }

    @Test
    void shouldPersistCategoryRelationship() {
        CategoryEntity category = createAndSaveCategory("Sensors");
        DataEntity savedData = createAndSaveData("Temperature", 25.0);
        savedData.setCategory(category);

        flushAndClear();

        DataEntity found = dataRepository.findById(savedData.getId())
                .orElseThrow();

        assertThat(found.getCategory()).isNotNull();

        assertThat(found.getCategory().getId())
                .isEqualTo(category.getId());

        assertThat(found.getCategory().getName())
                .isEqualTo("Sensors");
    }

    @Test
    void shouldPersistTagRelationshipThroughAssociationEntity() {
        TagEntity savedTag = createAndSaveTag("urgent");
        DataEntity savedData = createAndSaveData("Temperature", 25.0);
        savedData.addTag(savedTag, "MANUAL");

        flushAndClear();

        DataEntity found = dataRepository.findById(savedData.getId())
                .orElseThrow();

        assertThat(found.getDataTags()).hasSize(1);

//        DataTagEntity dataTag = found.getDataTags()
//                .iterator()
//                .next();

        DataTagEntity dataTag = found.getDataTags().stream()
                .findFirst()
                .orElseThrow();

        assertThat(found.getDataTags())
                .hasSize(1)
                .element(0) // Selects the first item (also works for sets)
                .satisfies(dt -> {
                    assertThat(dt.getTag().getId()).isEqualTo(savedTag.getId());
                    assertThat(dt.getTag().getName()).isEqualTo("urgent");
                    assertThat(dt.getSource()).isEqualTo("MANUAL");
                    assertThat(dt.getCreatedAt()).isNotNull();
                });

        assertThat(dataTag.getTag().getId())
                .isEqualTo(savedTag.getId());

        assertThat(dataTag.getTag().getName())
                .isEqualTo("urgent");

        assertThat(dataTag.getSource())
                .isEqualTo("MANUAL");

        assertThat(dataTag.getCreatedAt())
                .isNotNull();
    }
}