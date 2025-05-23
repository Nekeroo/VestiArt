package com.project.vestiart.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.vestiart.enums.TypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    private String title;

    private String description;

    @Column(name = "idexterne")
    private String idExterne;

    private String image;

    private String pdf;

    private String tag1;

    private String tag2;

    @Enumerated(EnumType.STRING)
    private TypeEnum type;

    @Column(name = "date_create")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateCreate;
}
