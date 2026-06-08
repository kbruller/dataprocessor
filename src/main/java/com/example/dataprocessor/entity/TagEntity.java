package com.example.dataprocessor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "tag_entity")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public TagEntity(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "tag")
    private Set<DataTagEntity> dataTags = new HashSet<>();

}
