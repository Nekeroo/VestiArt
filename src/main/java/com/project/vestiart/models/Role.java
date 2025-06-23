package com.project.vestiart.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Role entity representing a user permission role")
public class Role {

    @Id
    @Column(name = "id", nullable = false)
    @Schema(description = "Unique identifier for the role", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @Schema(description = "Name of the role", example = "Admin")
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    @Schema(description = "Users assigned to this role", accessMode = Schema.AccessMode.READ_ONLY)
    private List<User> users;

}
