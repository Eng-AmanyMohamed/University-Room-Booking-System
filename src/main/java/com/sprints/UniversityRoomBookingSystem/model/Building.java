package com.sprints.UniversityRoomBookingSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="buildings")
@Setter
@Getter
@NoArgsConstructor
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long building_id;
    private String building_name;
    private String location;

    @OneToMany(mappedBy = "building")
    List<Room> rooms = new ArrayList<>();
}
