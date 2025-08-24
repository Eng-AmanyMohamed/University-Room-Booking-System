package com.sprints.UniversityRoomBookingSystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildingResponseDTO {
    private String buildingName;
    private String buildingLocation;
}
