package com.project.vestiart.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.vestiart.enums.TypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Schema(description = "Fashion idea entity representing an innovation concept")
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Unique identifier for the idea", accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @Schema(description = "Title of the fashion idea", example = "Sustainable Denim Collection")
    private String title;

    @Schema(description = "Detailed description of the fashion idea")
    private String description;

    @Column(name = "idexterneimage")
    @Schema(description = "External identifier for the idea image in storage")
    private String idExterneImage;

    @Column(name = "idexternepdf")
    @Schema(description = "External identifier for the idea PDF document in storage")
    private String idExternePdf;

    @Schema(description = "Image URL or reference")
    private String image;

    @Schema(description = "PDF document URL or reference")
    private String pdf;

    @Schema(description = "Primary tag/category for the idea", example = "Sustainable")
    private String tag1;

    @Schema(description = "Secondary tag/category for the idea", example = "Casual")
    private String tag2;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Type classification of the fashion idea")
    private TypeEnum type;

    @Column(name = "date_create")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(description = "Date and time when the idea was created", example = "01-01-2025 12:00:00")
    private LocalDateTime dateCreate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Schema(description = "User who created the idea")
    private User user;

    @Override
    public String toString() {
        return "Idea{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", idExterneImage='" + idExterneImage + '\'' +
                ", idExternePdf='" + idExternePdf + '\'' +
                ", image='" + image + '\'' +
                ", pdf='" + pdf + '\'' +
                ", tag1='" + tag1 + '\'' +
                ", tag2='" + tag2 + '\'' +
                ", type=" + type +
                ", dateCreate=" + dateCreate +
                ", user=" + user +
                '}';
    }
}
