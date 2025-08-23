package com.sprints.UniversityRoomBookingSystem.dto.response;
import com.sprints.UniversityRoomBookingSystem.model.BookingAction;
import com.sprints.UniversityRoomBookingSystem.model.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingHistoryResponseDTO {
    private Long historyId;
    private BookingStatus previousStatus;
    private BookingStatus newStatus;
    private BookingAction action;
    private Instant changedAt;
    private String reason;

    private Long bookingId;
    private String changedBy;
}
