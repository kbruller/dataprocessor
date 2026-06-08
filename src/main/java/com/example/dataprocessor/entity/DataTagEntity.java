package com.example.dataprocessor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "data_tags")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_id", nullable = false)
    private DataEntity data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private TagEntity tag;

    private LocalDateTime createdAt;

    private String source;
}