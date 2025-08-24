package com.sprints.UniversityRoomBookingSystem.controller;

import com.sprints.UniversityRoomBookingSystem.dto.request.BuildingCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.BuildingResponseDTO;
import com.sprints.UniversityRoomBookingSystem.service.Building.BuildingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/buildings")
public class BuildingController {

    private final BuildingServiceImpl buildingService;

    // Create
    @PostMapping
    public ResponseEntity<BuildingResponseDTO> createBuilding(@RequestBody BuildingCreateDTO buildingDto) {
        return ResponseEntity.ok(buildingService.createBuilding(buildingDto));
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<BuildingResponseDTO>> getAllBuildings() {
        return ResponseEntity.ok(buildingService.getAllBuildings());
    }

    // Read all active
    @GetMapping("/active")
    public ResponseEntity<List<BuildingResponseDTO>> getAllActiveBuildings() {
        return ResponseEntity.ok(buildingService.getAllActiveBuildings());
    }

    // Read by id
    @GetMapping("/{id}")
    public ResponseEntity<BuildingResponseDTO> getBuildingById(@PathVariable Long id) {
        return ResponseEntity.ok(buildingService.getBuildingById(id));
    }

    // Update
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<BuildingResponseDTO> updateBuilding(@PathVariable Long id,
                                                   @RequestBody BuildingCreateDTO buildingBto) {
        return ResponseEntity.ok(buildingService.updateBuilding(id, buildingBto));
    }

    // Hard Delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
        buildingService.deleteBuilding(id);
        return ResponseEntity.noContent().build();
    }

    // Soft Delete
    @PatchMapping("/{id}/soft-delete")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> softDeleteBuilding(@PathVariable Long id) {
        buildingService.softDeleteBuilding(id);
        return ResponseEntity.noContent().build();
    }

    // Restore
    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> restoreBuilding(@PathVariable Long id) {
        buildingService.restoreBuilding(id);
        return ResponseEntity.ok().build();
    }

}
