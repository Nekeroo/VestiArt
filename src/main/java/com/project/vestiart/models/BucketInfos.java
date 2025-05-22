package com.project.vestiart.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "bucketinfos")
public class BucketInfos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "idexterne")
    private String idExterne;

    private String url;

    private String tag1;

    private String tag2;

    private String tag3;

}
