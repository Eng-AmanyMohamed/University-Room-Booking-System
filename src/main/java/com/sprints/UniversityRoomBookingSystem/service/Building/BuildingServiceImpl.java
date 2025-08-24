package com.sprints.UniversityRoomBookingSystem.service.Building;

import com.sprints.UniversityRoomBookingSystem.Exception.DataNotFoundException;
import com.sprints.UniversityRoomBookingSystem.Exception.InvalidRequestException;
import com.sprints.UniversityRoomBookingSystem.dto.request.BuildingCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.BuildingResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Building;
import com.sprints.UniversityRoomBookingSystem.modelmapper.BuildingMapper;
import com.sprints.UniversityRoomBookingSystem.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl {
    private final BuildingRepository buildingRepository;

    private final BuildingMapper buildingMapper;

    // Create
    public BuildingResponseDTO createBuilding(BuildingCreateDTO buildingDto) {
        Building building = buildingMapper.toEntity(buildingDto);
        buildingRepository.save(building);
        return buildingMapper.toDTO(building);
    }

    // Read all
    public List<BuildingResponseDTO> getAllBuildings() {
        List<Building> allBuildings = buildingRepository.findAll();

        if (allBuildings.isEmpty()) {
            throw new DataNotFoundException("There are no Buildings");
        }

        return allBuildings.stream()
                .map(buildingMapper::toDTO)
                .toList();
    }


    // Read All Active Buildings
    public List<BuildingResponseDTO> getAllActiveBuildings() {
        List<Building> allBuildings = buildingRepository.findAllActive();
        if (allBuildings.isEmpty()) {
            throw new DataNotFoundException("There is no Active Buildings");
        }

        return allBuildings.stream()
                .map(buildingMapper::toDTO)
                .toList();
    }

    // Read by id
    public BuildingResponseDTO getBuildingById(Long id) {
        Building building = buildingRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Building not found with id " + id));

        return buildingMapper.toDTO(building);
    }

    // Update
    // Update
    public BuildingResponseDTO updateBuilding(Long id, BuildingCreateDTO updatedBuildingDto) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Building not found with id " + id));


        building.setBuilding_name(updatedBuildingDto.getBuildingName());
        building.setLocation(updatedBuildingDto.getLocation());

        buildingRepository.save(building);
        return buildingMapper.toDTO(building);
    }


    // Delete
    public void deleteBuilding(Long id) {
        if (!buildingRepository.existsById(id)) {
            throw new DataNotFoundException("Building not found with id " + id);
        }
        buildingRepository.deleteById(id);
    }

    // Soft Delete
    public void softDeleteBuilding(Long id) {
        if (!buildingRepository.existsById(id)) {
            throw new DataNotFoundException("Building not found with id " + id);
        }
        buildingRepository.softDeleteById(id);
    }

    // Restore Building
    public void restoreBuilding(Long id) {
        if (!buildingRepository.existsById(id)) {
            throw new DataNotFoundException("Building not found with id " + id);
        }

        Building building = buildingRepository.findById(id).get();
        if (!building.getIsDeleted()) {
            throw new InvalidRequestException("Building is already not Deleted");

        }
        buildingRepository.restoreById(id);
    }
}
