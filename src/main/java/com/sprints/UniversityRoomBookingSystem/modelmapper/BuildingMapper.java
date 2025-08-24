package com.sprints.UniversityRoomBookingSystem.modelmapper;

import com.sprints.UniversityRoomBookingSystem.dto.request.BuildingCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.BuildingResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Building;
import org.springframework.stereotype.Component;

@Component
public class BuildingMapper {

    public BuildingResponseDTO toDTO(Building building) {
        return new BuildingResponseDTO(
                building.getBuilding_name(),
                building.getLocation()
        );
    }

    public Building toEntity(BuildingCreateDTO dto) {
        Building building = new Building();
        building.setBuilding_name(dto.getBuildingName());
        building.setLocation(dto.getLocation());
        return building;
    }

}
