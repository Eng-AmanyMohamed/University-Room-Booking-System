package com.sprints.UniversityRoomBookingSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="departments")
@Setter
@Getter
@NoArgsConstructor

public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="depart_id")
    private Long departId;
    private String name;

    @OneToMany(mappedBy = "department")
    List<User> users = new ArrayList<>();
}
