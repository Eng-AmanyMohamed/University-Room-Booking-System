package com.sprints.UniversityRoomBookingSystem.service.Building;

import com.sprints.UniversityRoomBookingSystem.model.Building;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BuildingService {
    Building createBuilding(Building building);

    List<Building> getAllBuildings();

    Optional<Building> getBuildingById(Long id);

    Building updateBuilding(Long id, Building updatedBuilding);

    void deleteBuilding(Long id);

    //List<Building> getAllActiveBuildings();
    // void softDeleteBuilding(Long id);
    // void restoreBuilding(Long id);

}
