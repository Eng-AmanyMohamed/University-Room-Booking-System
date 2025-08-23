package com.sprints.UniversityRoomBookingSystem.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponseDTO {
    private Long roomId;
    private String name;
    private Integer capacity;
    private String buildingName;
    private List<String> features;

}
