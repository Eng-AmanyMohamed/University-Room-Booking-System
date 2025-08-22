package com.sprints.UniversityRoomBookingSystem.controller;

import com.sprints.UniversityRoomBookingSystem.dto.request.BookingCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.BookingResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Booking;
import com.sprints.UniversityRoomBookingSystem.service.Booking.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {


        private final BookingService bookingService;

        // ---------------- CREATE ----------------
        @PostMapping
        public ResponseEntity<BookingResponseDTO> createBooking(
                @Valid @RequestBody BookingCreateDTO bookingCreateDTO) {
            return ResponseEntity.ok(bookingService.createBooking(bookingCreateDTO));
        }

        // ---------------- GET ALL ----------------
        @GetMapping
        public ResponseEntity<List<Booking>> getAllBookings() {
            return ResponseEntity.ok(bookingService.getAllBookings());
        }

        // ---------------- GET BY ID ----------------
        @GetMapping("/{id}")
        public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
            Booking booking = bookingService.getBookingById(id);
            if (booking == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(booking);
        }

        // ---------------- UPDATE ----------------
        @PutMapping("/{id}")
        public ResponseEntity<BookingResponseDTO> updateBooking(
                @PathVariable Long id,
                @Valid @RequestBody BookingCreateDTO bookingUpdateDTO) {
            return ResponseEntity.ok(bookingService.updateBooking(id, bookingUpdateDTO));
        }

        // ---------------- CANCEL ----------------
        @PutMapping("/{id}/cancel")
        public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
            bookingService.cancelBooking(id);
            return ResponseEntity.ok("Booking cancelled successfully.");
        }
}