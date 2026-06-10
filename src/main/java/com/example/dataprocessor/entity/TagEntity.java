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
@NoArgsConstructor(access = AccessLevel.PROTECTED) // <- for JPA a  protected!
@AllArgsConstructor(access = AccessLevel.PRIVATE)  // <- no acces from outside
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "tag")
    @Builder.Default
    private Set<DataTagEntity> dataTags = new HashSet<>();

}
