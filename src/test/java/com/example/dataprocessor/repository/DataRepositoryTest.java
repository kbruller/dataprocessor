package com.example.dataprocessor.repository;

import com.example.dataprocessor.entity.CategoryEntity;
import com.example.dataprocessor.entity.DataEntity;
import com.example.dataprocessor.entity.DataTagEntity;
import com.example.dataprocessor.entity.TagEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DataRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void shouldSaveAndFindDataEntity() {
        DataEntity entity = DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();

        DataEntity saved = dataRepository.save(entity);

        // ---------------------
        entityManager.flush(); // We force Hibernate to write the SQL to the database
        entityManager.clear(); // We completely clear Hibernate's internal memory (cache)
        // ---------------------

        assertThat(saved.getId()).isNotNull();

        DataEntity found = dataRepository.findById(saved.getId())
                .orElseThrow();

        assertThat(found.getName()).isEqualTo("Temperature");
        assertThat(found.getValue()).isEqualTo(25.0);
    }

    @Test
    void shouldPersistCategoryRelationship() {

        CategoryEntity category = CategoryEntity.builder()
                .name("Sensors")
                .build();

        category = categoryRepository.save(category);

        DataEntity data = DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();

        data.setCategory(category);

        DataEntity saved = dataRepository.save(data);

        // ---------------------
        entityManager.flush(); // We force Hibernate to write the SQL to the database
        entityManager.clear(); // We completely clear Hibernate's internal memory (cache)
        // ---------------------

        DataEntity found = dataRepository.findById(saved.getId())
                .orElseThrow();

        assertThat(found.getCategory()).isNotNull();

        assertThat(found.getCategory().getId())
                .isEqualTo(category.getId());

        assertThat(found.getCategory().getName())
                .isEqualTo("Sensors");
    }

    @Test
    void shouldPersistTagRelationshipThroughAssociationEntity() {

        TagEntity tag = TagEntity.builder()
                .name("urgent")
                .build();

        TagEntity savedTag = tagRepository.save(tag);

        DataEntity data = DataEntity.builder()
                .name("Temperature")
                .value(25.0)
                .build();

        data.addTag(tag, "MANUAL");

        DataEntity saved = dataRepository.save(data);

        // ---------------------
        entityManager.flush(); // We force Hibernate to write the SQL to the database
        entityManager.clear(); // We completely clear Hibernate's internal memory (cache)
        //

        DataEntity found = dataRepository.findById(saved.getId())
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
                .isEqualTo(tag.getId());

        assertThat(dataTag.getTag().getName())
                .isEqualTo("urgent");

        assertThat(dataTag.getSource())
                .isEqualTo("MANUAL");

        assertThat(dataTag.getCreatedAt())
                .isNotNull();
    }
}