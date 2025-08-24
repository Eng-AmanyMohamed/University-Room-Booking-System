package com.sprints.UniversityRoomBookingSystem.service.Building;

import com.sprints.UniversityRoomBookingSystem.dto.request.BuildingCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.BuildingResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Building;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BuildingService {
    BuildingResponseDTO createBuilding(BuildingCreateDTO buildingDto);

    List<BuildingResponseDTO> getAllBuildings();

    BuildingResponseDTO getBuildingById(Long id);

    BuildingResponseDTO updateBuilding(Long id, BuildingCreateDTO updatedBuilding);

    void deleteBuilding(Long id);

    List<BuildingResponseDTO> getAllActiveBuildings();

    void softDeleteBuilding(Long id);

    void restoreBuilding(Long id);

}
