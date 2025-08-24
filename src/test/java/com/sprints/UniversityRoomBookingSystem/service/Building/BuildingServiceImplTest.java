package com.sprints.UniversityRoomBookingSystem.service.Building;

import com.sprints.UniversityRoomBookingSystem.Exception.DataNotFoundException;
import com.sprints.UniversityRoomBookingSystem.Exception.InvalidRequestException;
import com.sprints.UniversityRoomBookingSystem.dto.request.BuildingCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.BuildingResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Building;
import com.sprints.UniversityRoomBookingSystem.modelmapper.BuildingMapper;
import com.sprints.UniversityRoomBookingSystem.repository.BuildingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BuildingServiceImplTest {

    @Mock
    private BuildingRepository buildingRepository;

    @Mock
    private BuildingMapper buildingMapper;

    @InjectMocks
    private BuildingServiceImpl buildingService;

    private Building building;
    private BuildingCreateDTO buildingCreateDTO;
    private BuildingResponseDTO buildingResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        building = new Building();
        building.setBuildingId(1L);
        building.setBuilding_name("Science Hall");
        building.setLocation("North Campus");
        building.setIsDeleted(false);

        buildingCreateDTO = new BuildingCreateDTO();
        buildingCreateDTO.setBuildingName("Science Hall");
        buildingCreateDTO.setLocation("North Campus");

        buildingResponseDTO = new BuildingResponseDTO();
        buildingResponseDTO.setBuildingName("Science Hall");
        buildingResponseDTO.setBuildingLocation("North Campus");
    }

    @Test
    void testCreateBuilding() {
        when(buildingMapper.toEntity(any(BuildingCreateDTO.class))).thenReturn(building);
        when(buildingMapper.toDTO(any(Building.class))).thenReturn(buildingResponseDTO);
        when(buildingRepository.save(any(Building.class))).thenReturn(building);

        BuildingResponseDTO result = buildingService.createBuilding(buildingCreateDTO);

        assertNotNull(result);
        assertEquals("Science Hall", result.getBuildingName());
        verify(buildingRepository, times(1)).save(any(Building.class));
    }

    @Test
    void testGetAllBuildings() {
        when(buildingRepository.findAll()).thenReturn(List.of(building));
        when(buildingMapper.toDTO(any(Building.class))).thenReturn(buildingResponseDTO);

        List<BuildingResponseDTO> result = buildingService.getAllBuildings();

        assertEquals(1, result.size());
        verify(buildingRepository, times(1)).findAll();
    }

    @Test
    void testGetAllBuildingsThrowsExceptionWhenEmpty() {
        when(buildingRepository.findAll()).thenReturn(List.of());

        assertThrows(DataNotFoundException.class, () -> buildingService.getAllBuildings());
    }

    @Test
    void testGetBuildingById() {
        when(buildingRepository.findById(1L)).thenReturn(Optional.of(building));
        when(buildingMapper.toDTO(building)).thenReturn(buildingResponseDTO);

        BuildingResponseDTO result = buildingService.getBuildingById(1L);

        assertNotNull(result);
        assertEquals("Science Hall", result.getBuildingName());
    }

    @Test
    void testGetBuildingByIdThrowsException() {
        when(buildingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> buildingService.getBuildingById(1L));
    }

    @Test
    void testUpdateBuilding() {
        when(buildingRepository.findById(1L)).thenReturn(Optional.of(building));
        when(buildingRepository.save(any(Building.class))).thenReturn(building);
        when(buildingMapper.toDTO(building)).thenReturn(buildingResponseDTO);

        BuildingResponseDTO result = buildingService.updateBuilding(1L, buildingCreateDTO);

        assertEquals("Science Hall", result.getBuildingName());
        verify(buildingRepository, times(1)).save(any(Building.class));
    }

    @Test
    void testDeleteBuilding() {
        when(buildingRepository.existsById(1L)).thenReturn(true);

        buildingService.deleteBuilding(1L);

        verify(buildingRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBuildingThrowsException() {
        when(buildingRepository.existsById(1L)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> buildingService.deleteBuilding(1L));
    }

    @Test
    void testSoftDeleteBuilding() {
        when(buildingRepository.existsById(1L)).thenReturn(true);

        buildingService.softDeleteBuilding(1L);

        verify(buildingRepository, times(1)).softDeleteById(1L);
    }

    @Test
    void testRestoreBuilding() {
        building.setIsDeleted(true);
        when(buildingRepository.existsById(1L)).thenReturn(true);
        when(buildingRepository.findById(1L)).thenReturn(Optional.of(building));

        buildingService.restoreBuilding(1L);

        verify(buildingRepository, times(1)).restoreById(1L);
    }

    @Test
    void testRestoreBuildingThrowsInvalidRequestException() {
        building.setIsDeleted(false);
        when(buildingRepository.existsById(1L)).thenReturn(true);
        when(buildingRepository.findById(1L)).thenReturn(Optional.of(building));

        assertThrows(InvalidRequestException.class, () -> buildingService.restoreBuilding(1L));
    }
}
