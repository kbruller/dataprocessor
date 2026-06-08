package com.example.dataprocessor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category_entity")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public CategoryEntity(String name) {
        this.name = name;
    }

//    @OneToMany(mappedBy = "category")
//    @Builder.Default
//    private List<DataEntity> dataList = new ArrayList<>();


}