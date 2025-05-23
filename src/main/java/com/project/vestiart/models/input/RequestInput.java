package com.project.vestiart.models.input;

import com.project.vestiart.models.Idea;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "request_input")
public class RequestInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    private String person;

    private String reference;

    private int type;

    @OneToOne
    @JoinColumn(name = "idea_id")
    private Idea idea;

}
