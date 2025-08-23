package com.sprints.UniversityRoomBookingSystem.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RoomCreateDTO {

    private String name;
    @Min(value = 10)@Max(value = 500)
    @NotNull(message = "Capacity is required")
    private Integer capacity;
    private Long buildingId;              // Reference to Building
    private List<String> featureNames;
}
