package com.sprints.UniversityRoomBookingSystem.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildingCreateDTO {
    @NotBlank(message = "Building name is required")
    private String buildingName;

    @NotBlank(message = "Location is required")
    private String location;

}
