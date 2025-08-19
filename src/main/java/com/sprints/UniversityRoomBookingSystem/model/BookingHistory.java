package com.sprints.UniversityRoomBookingSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking_history")
@Setter
@Getter
@NoArgsConstructor
public class BookingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long history_id;
    private String action;   // CREATED, APPROVED, REJECTED, CANCELLED
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
