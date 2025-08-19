package com.sprints.UniversityRoomBookingSystem.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingHistoryResponseDTO {
    private Long historyId;
    private String action;
    private LocalDateTime timestamp;

    private Long bookingId;
    private Long userId;
}
