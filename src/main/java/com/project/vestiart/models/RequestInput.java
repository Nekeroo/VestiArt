package com.project.vestiart.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "request_input")
@Schema(description = "Request input entity representing inputs for fashion idea generation")
public class RequestInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Unique identifier for the request input", accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @Schema(description = "Target demographic or person type", example = "Young urban professionals")
    private String person;

    @Schema(description = "Reference or inspiration for the innovation", example = "Sustainable materials and minimalist design")
    private String reference;

    @Schema(description = "Type of fashion item/collection as an integer code")
    private int type;

    @OneToOne
    @JoinColumn(name = "idea_id")
    @Schema(description = "The idea generated from this request input")
    private Idea idea;

}
