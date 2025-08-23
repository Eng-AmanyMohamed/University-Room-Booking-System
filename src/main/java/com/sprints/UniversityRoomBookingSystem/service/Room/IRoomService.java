package com.sprints.UniversityRoomBookingSystem.service.Room;

import com.sprints.UniversityRoomBookingSystem.dto.request.RoomCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.RoomResponseDTO;

import java.util.List;

public interface IRoomService {
    RoomResponseDTO createRoom(RoomCreateDTO roomDto);
    RoomResponseDTO getRoom(Long id);
    List<RoomResponseDTO> getRooms();
    RoomResponseDTO updateRoom(Long id, RoomCreateDTO roomDto);
    void deleteRoom(Long id);
}
