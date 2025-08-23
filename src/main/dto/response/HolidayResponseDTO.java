package com.sprints.UniversityRoomBookingSystem.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayResponseDTO {
    private Long holiday_id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
}
