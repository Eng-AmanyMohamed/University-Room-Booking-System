package com.sprints.UniversityRoomBookingSystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "booking_history")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
<<<<<<< Updated upstream
    @Column(name="history_id")
    private Long historyId;
    private String action;   // CREATED, APPROVED, REJECTED, CANCELLED
    private LocalDateTime timestamp;
=======
    private Long history_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status")
    private BookingStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status")
    private BookingStatus newStatus;

    @Column(nullable = false)
    private BookingAction action;

    @Column(name = "changed_at", nullable = false)
    private Instant changedAt;

    @Column(length = 1000)
    private String reason;
>>>>>>> Stashed changes

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    @ManyToOne
    @JoinColumn(name = "changed_by")
    private User changedBy;


}
