package com.example.dataprocessor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "data_entity")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "numeric_value")
    private double value;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;

    @OneToMany(
            mappedBy = "data",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private Set<DataTagEntity> dataTags = new HashSet<>();

    public Set<DataTagEntity> getDataTags() {
        if (this.dataTags == null) {
            this.dataTags = new HashSet<>();
        }
        return this.dataTags;
    }

    public void addTag(TagEntity tag, String source) {
        DataTagEntity dataTag = DataTagEntity.builder()
                .data(this)
                .tag(tag)
                .source(source)
                .createdAt(LocalDateTime.now())
                .build();

        this.dataTags.add(dataTag);
    }


    public void clearTags() {
        this.dataTags.clear();
    }
}