package com.sprints.UniversityRoomBookingSystem.controller;

import com.sprints.UniversityRoomBookingSystem.model.Building;
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
    public ResponseEntity<Building> createBuilding(@RequestBody Building building) {
        return ResponseEntity.ok(buildingService.createBuilding(building));
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<Building>> getAllBuildings() {
        return ResponseEntity.ok(buildingService.getAllBuildings());
    }

    // Read all active
//    @GetMapping("/active")
//    public ResponseEntity<List<Building>> getAllActiveBuildings() {
//        return ResponseEntity.ok(buildingService.getAllActiveBuildings());
//    }

    // Read by id
    @GetMapping("/{id}")
    public ResponseEntity<Building> getBuildingById(@PathVariable Long id) {
        return buildingService.getBuildingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Building> updateBuilding(@PathVariable Long id,
                                                   @RequestBody Building building) {
        return ResponseEntity.ok(buildingService.updateBuilding(id, building));
    }

    // Hard Delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
        buildingService.deleteBuilding(id);
        return ResponseEntity.noContent().build();
    }

    // Soft Delete
//    @PatchMapping("/{id}/soft-delete")
//    @PreAuthorize("hasRole('Admin')")
//    public ResponseEntity<Void> softDeleteBuilding(@PathVariable Long id) {
//        buildingService.softDeleteBuilding(id);
//        return ResponseEntity.noContent().build();
//    }

    // Restore
//    @PatchMapping("/{id}/restore")
//    @PreAuthorize("hasRole('Admin')")
//    public ResponseEntity<Void> restoreBuilding(@PathVariable Long id) {
//        buildingService.restoreBuilding(id);
//        return ResponseEntity.ok().build();
//    }

}
