package com.sprints.UniversityRoomBookingSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="rooms")
@Setter
@Getter
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_id")
    private Long roomId;
    private String name;
    private Integer capacity;
    @OneToMany(mappedBy = "room")
    List<Booking> bookingList = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;
    @ManyToMany
    @JoinTable(
            name = "room_features",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id"))
    private List<RoomFeature> features = new ArrayList<>();


}
