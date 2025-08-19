package com.sprints.UniversityRoomBookingSystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomFeatureCreateDTO {
    private String featureName;
    private List<Long> roomIds;  // just IDs when creating/updating
}
