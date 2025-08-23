package com.sprints.UniversityRoomBookingSystem.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;

    @Column(nullable = false, length = 50)
    private String name;
}
