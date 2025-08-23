package com.sprints.UniversityRoomBookingSystem.service.RoomFeature;

import com.sprints.UniversityRoomBookingSystem.dto.request.RoomCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.request.RoomFeatureCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.RoomFeatureResponseDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.RoomResponseDTO;

import java.util.List;

public interface IRoomFeatureService {
    RoomFeatureResponseDTO createRoomFeature(RoomFeatureCreateDTO featureDto);
    List<RoomFeatureResponseDTO> getRoomFeatures();
    RoomFeatureResponseDTO updateRoom(Long id, RoomFeatureCreateDTO featureDto);
    void deleteRoomFeature(Long id);
}
