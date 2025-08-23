package com.sprints.UniversityRoomBookingSystem.modelmapper;

import com.sprints.UniversityRoomBookingSystem.dto.response.RoomFeatureResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Room;
import com.sprints.UniversityRoomBookingSystem.model.RoomFeature;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RoomFeatureMapper {

    public RoomFeatureResponseDTO toRoomFeatureResponseDTO(RoomFeature roomFeature) {
        RoomFeatureResponseDTO roomFeatureResponseDTO = new RoomFeatureResponseDTO();
        roomFeatureResponseDTO.setFeatureName(roomFeature.getFeatureName());
        roomFeatureResponseDTO.setFeatureDescription(roomFeature.getFeatureDescription());
        roomFeatureResponseDTO.setFeature_id(roomFeature.getFeatureId());
        roomFeatureResponseDTO.setRoomIds(
                roomFeature.getRooms()
                        .stream()
                        .map(Room::getRoomId)
                        .toList()
        );
        return roomFeatureResponseDTO;
    }
}
