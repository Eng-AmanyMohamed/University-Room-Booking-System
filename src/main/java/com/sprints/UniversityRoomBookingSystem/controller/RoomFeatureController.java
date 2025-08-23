package com.sprints.UniversityRoomBookingSystem.controller;

import com.sprints.UniversityRoomBookingSystem.dto.request.RoomFeatureCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.RoomFeatureResponseDTO;
import com.sprints.UniversityRoomBookingSystem.service.RoomFeature.RoomFeatureService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/features")
@AllArgsConstructor
public class RoomFeatureController {
    private RoomFeatureService roomFeatureService;

    @PostMapping("/add")
    public ResponseEntity<RoomFeatureResponseDTO> addfeature(@Valid @RequestBody RoomFeatureCreateDTO roomFeatureCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomFeatureService.createRoomFeature(roomFeatureCreateDTO));
    }
    @GetMapping("/all")
        public ResponseEntity<List<RoomFeatureResponseDTO>> getFeatures(){
            return ResponseEntity.ok(roomFeatureService.getRoomFeatures());
        }
    @PutMapping("/{id}")
    public ResponseEntity<RoomFeatureResponseDTO> updatefeature(@PathVariable long id, @RequestBody RoomFeatureCreateDTO roomFeatureCreateDTO) {
        return ResponseEntity.ok(roomFeatureService.updateRoom(id, roomFeatureCreateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletefeature(@PathVariable long id) {
        roomFeatureService.deleteRoomFeature(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
