package com.sprints.UniversityRoomBookingSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="booking")
@Setter
@Getter
@NoArgsConstructor

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="booking_id")
    private Long bookingId;
    private String purpose;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;


    @OneToMany(mappedBy = "booking")
    List<BookingHistory> bookingHistoryList = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
