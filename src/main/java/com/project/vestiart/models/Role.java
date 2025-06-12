package com.project.vestiart.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {

    @Id
    @Column(name = "id", nullable = false)
    private int id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

}
