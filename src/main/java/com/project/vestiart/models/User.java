package com.project.vestiart.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Schema(description = "User entity representing a system user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Unique identifier for the user", accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @Schema(description = "Username for authentication and identification", example = "john_doe")
    private String username;

    @Schema(description = "User's encrypted password", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @Schema(description = "List of roles assigned to the user")
    private List<Role> roles;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
