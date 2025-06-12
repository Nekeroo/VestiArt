package com.project.vestiart.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

}
