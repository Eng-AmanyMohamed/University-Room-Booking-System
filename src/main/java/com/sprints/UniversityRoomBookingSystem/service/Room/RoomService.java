package com.sprints.UniversityRoomBookingSystem.service.Room;

import com.sprints.UniversityRoomBookingSystem.dto.request.RoomCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.request.RoomFeatureCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.RoomFeatureResponseDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.RoomResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.*;
import com.sprints.UniversityRoomBookingSystem.modelmapper.RoomMapper;
import com.sprints.UniversityRoomBookingSystem.repository.BuildingRepository;
import com.sprints.UniversityRoomBookingSystem.repository.RoomFeatureRepository;
import com.sprints.UniversityRoomBookingSystem.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomService implements IRoomService {

    private RoomRepository roomRepository;
    private RoomFeatureRepository roomFeatureRepository;
    private RoomMapper roomMapper;
    private BuildingRepository buildingRepository;


    @Override
    public RoomResponseDTO createRoom(RoomCreateDTO roomDto) {
        if (roomRepository.existsByNameIgnoreCase(roomDto.getName())) {
            throw new RuntimeException("A Room with that name already exists");
        }


        Building building=buildingRepository.findById(roomDto.getBuildingId())
                .orElseThrow(() -> new RuntimeException("Building Not Found"));

        List<String> roomFeaturesNames = roomDto.getFeatureNames();
        List<RoomFeature> roomFeatures = new ArrayList<>();
        for (String roomFeatureName : roomFeaturesNames) {
            if(roomFeatureRepository.existsByfeatureNameIgnoreCase(roomFeatureName)){
                RoomFeature roomFeature = roomFeatureRepository.findByfeatureNameIgnoreCase(roomFeatureName);
                roomFeatures.add(roomFeature);
            }else{
                throw new RuntimeException("Room Feature Not Found"); //Custom Exception
            }
        }

        Room room = new Room();
        room.setName(roomDto.getName());
        room.setCapacity(roomDto.getCapacity());
        room.setBuilding(building);
        room.setFeatures(roomFeatures);

        roomRepository.save(room);
        RoomResponseDTO roomResponseDTO = roomMapper.toRoomResponseDTO(room);

        return roomResponseDTO;
    }

    @Override
    public RoomResponseDTO getRoom(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room with id " + id + " not found"));
        RoomResponseDTO roomResponseDTO = roomMapper.toRoomResponseDTO(room);

        return roomResponseDTO;
    }

    @Override
    public List<RoomResponseDTO> getRooms() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomResponseDTO> roomResponseDTOs = new ArrayList<>();
        for (Room room : rooms) {
            RoomResponseDTO roomResponseDTO = roomMapper.toRoomResponseDTO(room);
            roomResponseDTOs.add(roomResponseDTO);
        }
        return roomResponseDTOs;
    }

    @Override
    public RoomResponseDTO updateRoom(Long id, RoomCreateDTO roomDto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room with id " + id + " not found"));



        Building building=buildingRepository.findById(roomDto.getBuildingId())
                .orElseThrow(() -> new RuntimeException("Building Not Found"));

        List<String> roomFeaturesNames = roomDto.getFeatureNames();
        List<RoomFeature> roomFeatures = new ArrayList<>();
        for (String roomFeatureName : roomFeaturesNames) {
            if(roomFeatureRepository.existsByfeatureNameIgnoreCase(roomFeatureName)){
                RoomFeature roomFeature = roomFeatureRepository.findByfeatureNameIgnoreCase(roomFeatureName);
                roomFeatures.add(roomFeature);
            }else{
                throw new RuntimeException("Room Feature Not Found"); //Custom Exception
            }
        }

        room.setName(roomDto.getName());
        room.setCapacity(roomDto.getCapacity());
        room.setBuilding(building);
        room.setFeatures(roomFeatures);

        roomRepository.save(room);
        RoomResponseDTO roomResponseDTO = roomMapper.toRoomResponseDTO(room);
        return roomResponseDTO;
    }

    @Override
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not Found"));
        List<Booking> bookings = room.getBookingList();
        for (Booking booking : bookings) {
            if (booking.getStatus() == BookingStatus.APPROVED && booking.getEnd_time().isAfter(LocalDateTime.now())) {
                throw new RuntimeException("Booking has already been approved"); //replace with custom exception
            }
        } roomRepository.delete(room);

    }
}
