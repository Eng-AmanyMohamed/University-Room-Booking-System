package com.sprints.UniversityRoomBookingSystem.dto.response;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sprints.UniversityRoomBookingSystem.model.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDTO {
    private Long booking_id;
    private String purpose;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime start_time;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime end_time;

    private BookingStatus status;
    private Long room_id;
    private String room_name;
    private Long user_id;
    private String username;
}
