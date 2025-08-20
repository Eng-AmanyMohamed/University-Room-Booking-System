package com.sprints.UniversityRoomBookingSystem.controller;

import com.sprints.UniversityRoomBookingSystem.dto.request.HolidayCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.HolidayResponseDTO;
import com.sprints.UniversityRoomBookingSystem.service.Holiday.HolidayService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {
    private final HolidayService holidayService;
    @Autowired
    public HolidayController(HolidayService holidayService)
    {
        this.holidayService = holidayService;
    }
    @PostMapping
    public ResponseEntity<HolidayResponseDTO> createHoliday(@Valid @RequestBody HolidayCreateDTO holidayCreateDTO) {
        return ResponseEntity.ok(holidayService.createHoliday(holidayCreateDTO));
    }

    @GetMapping
    public ResponseEntity<List<HolidayResponseDTO>> getAllHolidays() {
        return ResponseEntity.ok(holidayService.getAllHolidays());
    }
    @GetMapping("/{id}")
    public ResponseEntity<HolidayResponseDTO> getHolidayById(@PathVariable Long id) {
        return ResponseEntity.ok(holidayService.getHolidayById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<HolidayResponseDTO>updateHoliday(@PathVariable Long id, @RequestBody HolidayCreateDTO holidayCreateDTO){
        HolidayResponseDTO updatedHoliday = holidayService.updateHoliday(id, holidayCreateDTO);
        return ResponseEntity.ok(updatedHoliday);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String>deleteHoliday(@PathVariable Long id)
    {
        holidayService.deleteHoliday(id);
        return ResponseEntity.ok("Holiday deleted successfully");
    }

}
