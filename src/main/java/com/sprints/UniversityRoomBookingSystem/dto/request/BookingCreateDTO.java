package com.sprints.UniversityRoomBookingSystem.dto.request;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@NoOverlap   you should IMPLEMENT this custom annotation to prevent overlapping bookings
public class BookingCreateDTO {
    @NotBlank(message = "Purpose is required")
    private String purpose;

    @NotNull(message = "Start time is required")
    @FutureOrPresent(message = "Start time must be in the present or future")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime start_time;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime end_time;

    @NotNull(message = "Room ID is required")
    private Long room_id;

    @NotNull(message = "User ID is required")
    private Long user_id;
}
