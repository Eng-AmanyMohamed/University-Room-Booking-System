package com.sprints.UniversityRoomBookingSystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RoomFeatureResponseDTO {
    private Long feature_id;
    private String featureName;
    private List<Long> roomIds;
}
