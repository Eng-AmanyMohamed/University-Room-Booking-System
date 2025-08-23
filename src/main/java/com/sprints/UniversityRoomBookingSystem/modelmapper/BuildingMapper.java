//package com.sprints.UniversityRoomBookingSystem.modelmapper;
//
//import com.sprints.UniversityRoomBookingSystem.dto.request.BuildingCreateDTO;
//import com.sprints.UniversityRoomBookingSystem.model.Building;
//
//public class BuildingMapper {
//
//    public static BuildingCreateDTO toDTO(Building building) {
//        return new BuildingCreateDTO(
//                building.getBuilding_name(),
//                building.getLocation(),
//                building.getRooms()
//        );
//    }
//
//    public static Building toEntity(BuildingCreateDTO dto) {
//        Building building = new Building();
//        building.setBuilding_name(dto.getBuildingName());
//        building.setLocation(dto.getLocation());
//        building.setRooms(dto.getRoomIds());
//        return building;
//    }
//
//}
