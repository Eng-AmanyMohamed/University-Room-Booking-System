package com.sprints.UniversityRoomBookingSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="roomFeature")
@Setter
@Getter
@NoArgsConstructor

public class RoomFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="feature_id")
    private Long featureId;
    private String featureName;
    private String featureDescription;
    @ManyToMany
    @JoinTable(
            name = "room_features",
            joinColumns = @JoinColumn(name = "feature_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private List<Room> rooms = new ArrayList<>();
}
