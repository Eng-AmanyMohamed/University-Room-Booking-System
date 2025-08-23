package com.sprints.UniversityRoomBookingSystem.service.Building;

import com.sprints.UniversityRoomBookingSystem.Exception.DataNotFoundException;
import com.sprints.UniversityRoomBookingSystem.Exception.InvalidRequestException;
import com.sprints.UniversityRoomBookingSystem.model.Building;
import com.sprints.UniversityRoomBookingSystem.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl {
    private final BuildingRepository buildingRepository;

    // Create
    public Building createBuilding(Building building) {
        return buildingRepository.save(building);
    }

    // Read all
    public List<Building> getAllBuildings() {
        List<Building> allBuildings = buildingRepository.findAll();
        if(allBuildings.isEmpty()){
            throw new DataNotFoundException("There is no Buildings");
        }
        return allBuildings;
    }

    // Read All Active Buildings
//    public List<Building> getAllActiveBuildings() {
//        List<Building> allBuildings = buildingRepository.findAllActive();
//        if(allBuildings.isEmpty()){
//            throw new DataNotFoundException("There is no Active Buildings");
//        }
//        return allBuildings;
//    }

    // Read by id
    public Optional<Building> getBuildingById(Long id) {
        return buildingRepository.findById(id);
    }

    // Update
    public Building updateBuilding(Long id, Building updatedBuilding) {
        return buildingRepository.findById(id)
                .map(existing -> {
                    existing.setBuilding_name(updatedBuilding.getBuilding_name());
                    existing.setLocation(updatedBuilding.getLocation());
                    existing.setRooms(updatedBuilding.getRooms());
                    return buildingRepository.save(existing);
                })
                .orElseThrow(() -> new DataNotFoundException("Building not found with id " + id));
    }

    // Delete
    public void deleteBuilding(Long id) {
        if (!buildingRepository.existsById(id)) {
            throw new DataNotFoundException("Building not found with id " + id);
        }
        buildingRepository.deleteById(id);
    }

    // Soft Delete
//    public void siftDeleteBuilding(Long id) {
//        if (!buildingRepository.existsById(id)) {
//            throw new DataNotFoundException("Building not found with id " + id);
//        }
//        buildingRepository.softDeleteById(id);
//    }

    // Restore Building
//    public void restoreBuilding(Long id) {
//        if (!buildingRepository.existsById(id)) {
//            throw new DataNotFoundException("Building not found with id " + id);
//        }
//
//        Building building = buildingRepository.findById(id).get();
//        if(!building.isDeleted){
//            throw new InvalidRequestException("Building is already not Deleted");
//
//        }
//        buildingRepository.restoreBuilding(id);
//    }
}
