package com.sprints.UniversityRoomBookingSystem.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private Long roleId;

    @Column(nullable = false, length = 50)
    private String name;
}
