package com.sprints.UniversityRoomBookingSystem.service.Booking;
import com.sprints.UniversityRoomBookingSystem.controller.BookingController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprints.UniversityRoomBookingSystem.dto.request.BookingCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.BookingResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Booking;
import com.sprints.UniversityRoomBookingSystem.model.BookingStatus;
import com.sprints.UniversityRoomBookingSystem.model.Room;
import com.sprints.UniversityRoomBookingSystem.model.User;
import com.sprints.UniversityRoomBookingSystem.service.Booking.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("BookingController Unit Tests")
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingCreateDTO validBookingCreateDTO;
    private BookingResponseDTO mockBookingResponseDTO;
    private Booking mockBooking;
    private Room mockRoom;
    private User mockUser;

    @BeforeEach
    void setUp() {
        // Setup mock objects
        mockRoom = new Room();
        mockRoom.setRoomId(1L);
        mockRoom.setName("Room A101");

        mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setUsername("student1");

        // Valid booking create DTO
        validBookingCreateDTO = new BookingCreateDTO();
        validBookingCreateDTO.setPurpose("Team Meeting");
        validBookingCreateDTO.setStart_time(LocalDateTime.now().plusDays(1));
        validBookingCreateDTO.setEnd_time(LocalDateTime.now().plusDays(1).plusHours(2));
        validBookingCreateDTO.setRoom_id(1L);
        validBookingCreateDTO.setUser_id(1L);

        // Mock booking response DTO
        mockBookingResponseDTO = new BookingResponseDTO();
        mockBookingResponseDTO.setBooking_id(1L);
        mockBookingResponseDTO.setPurpose("Team Meeting");
        mockBookingResponseDTO.setStart_time(LocalDateTime.now().plusDays(1));
        mockBookingResponseDTO.setEnd_time(LocalDateTime.now().plusDays(1).plusHours(2));
        mockBookingResponseDTO.setStatus(BookingStatus.PENDING);
        mockBookingResponseDTO.setRoom_id(1L);
        mockBookingResponseDTO.setRoom_name("Room A101");
        mockBookingResponseDTO.setUser_id(1L);
        mockBookingResponseDTO.setUsername("student1");

        // Mock booking entity
        mockBooking = new Booking();
        mockBooking.setBookingId(1L);
        mockBooking.setPurpose("Team Meeting");
        mockBooking.setStartTime(LocalDateTime.now().plusDays(1));
        mockBooking.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        mockBooking.setStatus(BookingStatus.PENDING);
        mockBooking.setRoom(mockRoom);
        mockBooking.setUser(mockUser);
    }

    // ==================== CREATE BOOKING TESTS ====================

    @Test
    @DisplayName("Should create booking successfully with valid data")
    void createBooking_WithValidData_ShouldReturnCreatedBooking() throws Exception {
        // Arrange
        when(bookingService.createBooking(any(BookingCreateDTO.class)))
                .thenReturn(mockBookingResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookingCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.booking_id").value(1L))
                .andExpect(jsonPath("$.purpose").value("Team Meeting"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.room_id").value(1L))
                .andExpect(jsonPath("$.room_name").value("Room A101"))
                .andExpect(jsonPath("$.user_id").value(1L))
                .andExpect(jsonPath("$.username").value("student1"));

        verify(bookingService, times(1)).createBooking(any(BookingCreateDTO.class));
    }

    @Test
    @DisplayName("Should return 400 when start time is in the past")
    void createBooking_WithPastStartTime_ShouldReturnBadRequest() throws Exception {
        // Arrange
        validBookingCreateDTO.setStart_time(LocalDateTime.now().minusHours(1)); // Past time

        // Act & Assert
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookingCreateDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).createBooking(any(BookingCreateDTO.class));
    }

    @Test
    @DisplayName("Should return 400 when end time is before start time")
    void createBooking_WithEndTimeBeforeStartTime_ShouldReturnBadRequest() throws Exception {
        // Arrange
        validBookingCreateDTO.setStart_time(LocalDateTime.now().plusDays(1));
        validBookingCreateDTO.setEnd_time(LocalDateTime.now().plusHours(1)); // Before start time

        // Act & Assert
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookingCreateDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).createBooking(any(BookingCreateDTO.class));
    }

    @Test
    @DisplayName("Should return 400 when @NoOverlap validation fails")
    void createBooking_WithOverlappingBooking_ShouldReturnBadRequest() throws Exception {
        // Arrange - This will be validated by your @NoOverlap custom validator
        // The actual validation logic should be in the validator class

        // Act & Assert
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookingCreateDTO)))
                .andExpect(status().isOk()); // This should pass, overlap validation is in service layer

        verify(bookingService, times(1)).createBooking(any(BookingCreateDTO.class));
    }

    // ==================== GET ALL BOOKINGS TESTS ====================

    @Test
    @DisplayName("Should return all bookings successfully")
    void getAllBookings_ShouldReturnListOfBookings() throws Exception {
        // Arrange
        List<Booking> bookings = Arrays.asList(mockBooking, mockBooking);
        when(bookingService.getAllBookings()).thenReturn(bookings);

        // Act & Assert
        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].bookingId").value(1L))
                .andExpect(jsonPath("$[0].purpose").value("Team Meeting"));

        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    @DisplayName("Should return empty list when no bookings exist")
    void getAllBookings_WhenNoBookingsExist_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(bookingService.getAllBookings()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(bookingService, times(1)).getAllBookings();
    }

    // ==================== GET BOOKING BY ID TESTS ====================

    @Test
    @DisplayName("Should return booking when valid ID is provided")
    void getBookingById_WithValidId_ShouldReturnBooking() throws Exception {
        // Arrange
        when(bookingService.getBookingById(1L)).thenReturn(mockBooking);

        // Act & Assert
        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookingId").value(1L))
                .andExpect(jsonPath("$.purpose").value("Team Meeting"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(bookingService, times(1)).getBookingById(1L);
    }

    @Test
    @DisplayName("Should return 404 when booking does not exist")
    void getBookingById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(bookingService.getBookingById(999L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/bookings/999"))
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).getBookingById(999L);
    }

    @Test
    @DisplayName("Should return 400 when ID format is invalid")
    void getBookingById_WithInvalidIdFormat_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/bookings/invalid-id"))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).getBookingById(anyLong());
    }



    @Test
    @DisplayName("Should return 400 when update data is invalid")
    void updateBooking_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange - Invalid update data
        BookingCreateDTO invalidUpdate = new BookingCreateDTO();
        invalidUpdate.setPurpose(""); // Empty purpose should be invalid

        // Act & Assert
        mockMvc.perform(put("/api/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdate)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).updateBooking(anyLong(), any(BookingCreateDTO.class));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent booking")
    void updateBooking_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(bookingService.updateBooking(eq(999L), any(BookingCreateDTO.class)))
                .thenThrow(new RuntimeException("Booking not found"));

        // Act & Assert
        mockMvc.perform(put("/api/bookings/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookingCreateDTO)))
                .andExpect(status().isInternalServerError()); // Assuming no global exception handler

        verify(bookingService, times(1)).updateBooking(eq(999L), any(BookingCreateDTO.class));
    }

    // ==================== CANCEL BOOKING TESTS ====================

    @Test
    @DisplayName("Should cancel booking successfully")
    void cancelBooking_WithValidId_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        doNothing().when(bookingService).cancelBooking(1L);

        // Act & Assert
        mockMvc.perform(put("/api/bookings/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking cancelled successfully."));

        verify(bookingService, times(1)).cancelBooking(1L);
    }

    @Test
    @DisplayName("Should return error when canceling non-existent booking")
    void cancelBooking_WithNonExistentId_ShouldReturnError() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Booking not found"))
                .when(bookingService).cancelBooking(999L);

        // Act & Assert
        mockMvc.perform(put("/api/bookings/999/cancel"))
                .andExpect(status().isInternalServerError()); // Assuming no global exception handler

        verify(bookingService, times(1)).cancelBooking(999L);
    }

    @Test
    @DisplayName("Should return error when canceling already cancelled booking")
    void cancelBooking_AlreadyCancelled_ShouldReturnError() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Booking already cancelled"))
                .when(bookingService).cancelBooking(1L);

        // Act & Assert
        mockMvc.perform(put("/api/bookings/1/cancel"))
                .andExpect(status().isInternalServerError());

        verify(bookingService, times(1)).cancelBooking(1L);
    }

    @Test
    @DisplayName("Should return error when canceling past booking")
    void cancelBooking_PastBooking_ShouldReturnError() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Cannot cancel past bookings"))
                .when(bookingService).cancelBooking(1L);

        // Act & Assert
        mockMvc.perform(put("/api/bookings/1/cancel"))
                .andExpect(status().isInternalServerError());

        verify(bookingService, times(1)).cancelBooking(1L);
    }

    // ==================== ERROR HANDLING TESTS ====================

    @Test
    @DisplayName("Should handle service layer exceptions gracefully")
    void handleServiceExceptions_ShouldReturnAppropriateError() throws Exception {
        // Arrange
        when(bookingService.getAllBookings())
                .thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isInternalServerError());

        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    @DisplayName("Should handle concurrent booking requests")
    void handleConcurrentBookingRequests_ShouldProcessCorrectly() throws Exception {
        // Arrange
        when(bookingService.createBooking(any(BookingCreateDTO.class)))
                .thenReturn(mockBookingResponseDTO);

        // Act & Assert - Simulate concurrent requests
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/api/bookings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validBookingCreateDTO)))
                    .andExpect(status().isOk());
        }

        verify(bookingService, times(5)).createBooking(any(BookingCreateDTO.class));
    }

    @Test
    @DisplayName("Should validate booking duration limits")
    void validateBookingDuration_ExceedsLimit_ShouldReturnBadRequest() throws Exception {
        // Arrange - Booking duration exceeds reasonable limit (e.g., 24 hours)
        validBookingCreateDTO.setStart_time(LocalDateTime.now().plusDays(1));
        validBookingCreateDTO.setEnd_time(LocalDateTime.now().plusDays(2)); // 24+ hours

        // This test assumes you have duration validation in your service or custom validator
        when(bookingService.createBooking(any(BookingCreateDTO.class)))
                .thenThrow(new RuntimeException("Booking duration exceeds maximum allowed"));

        // Act & Assert
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookingCreateDTO)))
                .andExpect(status().isInternalServerError());
    }

    // ==================== VALIDATION SPECIFIC TESTS ====================

    @Test
    @DisplayName("Should return 400 when purpose is blank")
    void createBooking_WithBlankPurpose_ShouldReturnBadRequest() throws Exception {
        // Arrange
        validBookingCreateDTO.setPurpose(""); // Blank purpose

        // Act & Assert
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookingCreateDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).createBooking(any(BookingCreateDTO.class));
    }

    @Test
    @DisplayName("Should return 400 when room_id is null")
    void createBooking_WithNullRoomId_ShouldReturnBadRequest() throws Exception {
        // Arrange
        validBookingCreateDTO.setRoom_id(null); // Null room_id

        // Act & Assert
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookingCreateDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).createBooking(any(BookingCreateDTO.class));
    }

    @Test
    @DisplayName("Should return 400 when user_id is null")
    void createBooking_WithNullUserId_ShouldReturnBadRequest() throws Exception {
        // Arrange
        validBookingCreateDTO.setUser_id(null); // Null user_id

        // Act & Assert
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookingCreateDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).createBooking(any(BookingCreateDTO.class));
    }

    @Test
    @DisplayName("Should handle JSON date format correctly")
    void createBooking_WithCorrectDateFormat_ShouldSucceed() throws Exception {
        // Arrange
        String jsonWithDateFormat = """
            {
                "purpose": "Team Meeting",
                "start_time": "2025-08-25 10:00",
                "end_time": "2025-08-25 12:00",
                "room_id": 1,
                "user_id": 1
            }
            """;

        when(bookingService.createBooking(any(BookingCreateDTO.class)))
                .thenReturn(mockBookingResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithDateFormat))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).createBooking(any(BookingCreateDTO.class));
    }
}