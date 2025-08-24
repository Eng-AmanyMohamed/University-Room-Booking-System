package com.sprints.UniversityRoomBookingSystem.controller;

import com.sprints.UniversityRoomBookingSystem.dto.response.BookingHistoryResponseDTO;
import com.sprints.UniversityRoomBookingSystem.service.BookhingHistory.BookingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookings/{bookingId}/history")
@RequiredArgsConstructor
public class BookingHistoryController {
    private final BookingHistoryService bookingHistoryService;

    @GetMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<BookingHistoryResponseDTO>>getBookingHistory(@PathVariable Long bookingId){
        List<BookingHistoryResponseDTO>historyList=bookingHistoryService.getHistoryForBooking(bookingId);
        return ResponseEntity.ok(historyList);
    }

}
