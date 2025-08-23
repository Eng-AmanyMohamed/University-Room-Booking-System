package com.sprints.UniversityRoomBookingSystem.modelmapper;

import com.sprints.UniversityRoomBookingSystem.dto.response.RoomResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Room;
import com.sprints.UniversityRoomBookingSystem.model.RoomFeature;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public RoomResponseDTO toRoomResponseDTO(Room room) {
        RoomResponseDTO roomResponseDTO = new RoomResponseDTO();
        roomResponseDTO.setName(room.getName());
        roomResponseDTO.setRoomId(room.getRoomId());
        roomResponseDTO.setBuildingName(room.getBuilding().getBuilding_name());
        roomResponseDTO.setFeatures(
                room.getFeatures()
                        .stream()
                        .map(RoomFeature::getFeatureName)
                        .toList()
        );

        return roomResponseDTO;

    }
}
