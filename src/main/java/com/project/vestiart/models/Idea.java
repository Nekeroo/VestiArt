package com.project.vestiart.models;

import com.project.vestiart.enums.TypeEnum;
import jakarta.persistence.*;
import lombok.*;

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

    private String image;

    private String tag1;

    private String tag2;

    @Enumerated(EnumType.STRING)
    private TypeEnum type;

}
