package com.example.dataprocessor.repository;

import com.example.dataprocessor.entity.CategoryEntity;
import com.example.dataprocessor.entity.DataEntity;
import com.example.dataprocessor.entity.DataTagEntity;
import com.example.dataprocessor.entity.TagEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@ActiveProfiles("postgres")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DataRepositoryPostgresTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void shouldPersistCategoryRelationshipWithPostgres() {
        CategoryEntity category = CategoryEntity.builder()
                .name("Sensors")
                .build();

        category = categoryRepository.save(category);

        DataEntity data = DataEntity.
                builder().
                name("Temperature").
                value(25.0).build();

        data.setCategory(category);

        DataEntity saved = dataRepository.save(data);

        DataEntity found = dataRepository.findById(saved.getId())
                .orElseThrow();

        assertThat(found.getCategory()).isNotNull();
        assertThat(found.getCategory().getName()).isEqualTo("Sensors");
    }

    @Test
    void shouldPersistTagRelationshipThroughAssociationEntityWithPostgres() {
        TagEntity tag = TagEntity.builder()
                .name("urgent")
                .build();

        tag = tagRepository.save(tag);

        DataEntity data = DataEntity.
                builder().
                name("Temperature").
                value(25.0).build();
        data.addTag(tag, "MANUAL");

        DataEntity saved = dataRepository.save(data);

        DataEntity found = dataRepository.findById(saved.getId())
                .orElseThrow();

        assertThat(found.getDataTags()).hasSize(1);

        DataTagEntity dataTag = found.getDataTags()
                .iterator()
                .next();

        assertThat(dataTag.getTag().getName()).isEqualTo("urgent");
        assertThat(dataTag.getSource()).isEqualTo("MANUAL");
        assertThat(dataTag.getCreatedAt()).isNotNull();
    }
}