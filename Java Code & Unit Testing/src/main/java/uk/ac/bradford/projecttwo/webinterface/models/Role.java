package uk.ac.bradford.projecttwo.webinterface.models;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;
// Role.java
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;
    
    private String roleName;
    private String department;
    private String description;
    
    @ManyToMany
    @JoinTable(
        name = "role_datasets",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "dataset_id")
    )
    private Set<Dataset> accessibleDatasets = new HashSet<>();
    
    // Getters and setters
}