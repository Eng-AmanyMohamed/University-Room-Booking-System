package com.sprints.UniversityRoomBookingSystem.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingHistoryCreateDTO {
    @NotBlank(message = "Action is required")
    private String action;   // CREATED, APPROVED, REJECTED, CANCELLED

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotNull(message = "User ID is required")
    private Long userId;
}
