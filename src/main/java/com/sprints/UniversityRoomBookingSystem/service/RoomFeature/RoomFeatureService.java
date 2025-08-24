package com.sprints.UniversityRoomBookingSystem.service.RoomFeature;

import com.sprints.UniversityRoomBookingSystem.Exception.DataNotFoundException;
import com.sprints.UniversityRoomBookingSystem.Exception.DuplicateException;
import com.sprints.UniversityRoomBookingSystem.dto.request.RoomCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.request.RoomFeatureCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.RoomFeatureResponseDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.RoomResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Room;
import com.sprints.UniversityRoomBookingSystem.model.RoomFeature;
import com.sprints.UniversityRoomBookingSystem.modelmapper.RoomFeatureMapper;
import com.sprints.UniversityRoomBookingSystem.repository.RoomFeatureRepository;
import com.sprints.UniversityRoomBookingSystem.service.Room.IRoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RoomFeatureService implements IRoomFeatureService {
    private RoomFeatureRepository roomFeatureRepository;
    private RoomFeatureMapper roomFeatureMapper;

    @Override
    public RoomFeatureResponseDTO createRoomFeature(RoomFeatureCreateDTO featureDto) {
        if(roomFeatureRepository.existsByFeatureNameIgnoreCase(featureDto.getFeatureName())){
            throw new DuplicateException("Room Feature already exists");
        }
        RoomFeature roomFeature = new RoomFeature();
        roomFeature.setFeatureName(featureDto.getFeatureName());
        roomFeature.setFeatureDescription(featureDto.getFeatureDescription());
        roomFeatureRepository.save(roomFeature);

        RoomFeatureResponseDTO roomFeatureResponseDTO = roomFeatureMapper.toRoomFeatureResponseDTO(roomFeature);

        return roomFeatureResponseDTO;
    }

    @Override
    public List<RoomFeatureResponseDTO> getRoomFeatures() {
        List<RoomFeature> features = roomFeatureRepository.findAll();
        List<RoomFeatureResponseDTO> roomFeatureResponseDTOS = new ArrayList<>();
        for (RoomFeature room : features) {
            RoomFeatureResponseDTO roomFeatureResponseDTO = roomFeatureMapper.toRoomFeatureResponseDTO(room);
            roomFeatureResponseDTOS.add(roomFeatureResponseDTO);
        }
        return roomFeatureResponseDTOS;

    }

    @Override
    public RoomFeatureResponseDTO updateRoom(Long id, RoomFeatureCreateDTO featureDto) {
        RoomFeature feature = roomFeatureRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Room Feature not Found"));
        feature.setFeatureName(featureDto.getFeatureName());
        feature.setFeatureDescription(featureDto.getFeatureDescription());
        roomFeatureRepository.save(feature);
        RoomFeatureResponseDTO roomFeatureResponseDTO = roomFeatureMapper.toRoomFeatureResponseDTO(feature);
        return roomFeatureResponseDTO;
    }

    @Override
    public void deleteRoomFeature(Long id) {
        RoomFeature feature = roomFeatureRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Room Feature not Found"));
        roomFeatureRepository.delete(feature);
    }
}
