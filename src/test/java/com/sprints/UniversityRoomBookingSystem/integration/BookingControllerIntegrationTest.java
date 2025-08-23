package com.sprints.UniversityRoomBookingSystem.integration;

import com.sprints.UniversityRoomBookingSystem.dto.response.BookingResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Booking;
import com.sprints.UniversityRoomBookingSystem.repository.RoomRepository;
import com.sprints.UniversityRoomBookingSystem.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprints.UniversityRoomBookingSystem.dto.request.BookingCreateDTO;
import com.sprints.UniversityRoomBookingSystem.model.BookingStatus;
import com.sprints.UniversityRoomBookingSystem.model.Room;
import com.sprints.UniversityRoomBookingSystem.model.User;
import com.sprints.UniversityRoomBookingSystem.service.Booking.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private BookingService bookingService; // 👈 mocked service

    private Room room;
    private User user;
    private Booking booking;

    @BeforeEach
    void setUp() {
        // Persist Room
        room = new Room();
        room.setName("Room A");
        room.setCapacity(20);
        entityManager.persist(room);

        // Persist User
        user = new User();
        user.setUsername("Alice");
        user.setEmail("alice@example.com");
        user.setPassword("alice123");
        entityManager.persist(user);

        booking = new Booking();
        booking.setPurpose("Lecture");
        booking.setStartTime(LocalDateTime.of(2025, 9, 10, 10, 0));
        booking.setEndTime(LocalDateTime.of(2025, 9, 12, 10, 0));
        booking.setRoom(room);
        booking.setUser(user);
        booking.setStatus(BookingStatus.PENDING);
        entityManager.persist(booking);

        entityManager.flush();
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateBooking() throws Exception {
        BookingCreateDTO dto = new BookingCreateDTO();
        dto.setPurpose("Lecture 2");
        dto.setStart_time(LocalDateTime.of(2025, 9, 15, 10, 0));
        dto.setEnd_time(LocalDateTime.of(2025, 9, 20, 10, 0));
        dto.setRoom_id(room.getRoomId());
        dto.setUser_id(user.getUserId());

        BookingResponseDTO responseDTO = new BookingResponseDTO(
                2L, "Lecture 2", dto.getStart_time(), dto.getEnd_time(),
                BookingStatus.PENDING, room.getRoomId(), room.getName(),
                user.getUserId(), user.getUsername()
        );

        when(bookingService.createBooking(any(BookingCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/bookings")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking_id").value(2L))
                .andExpect(jsonPath("$.purpose").value("Lecture 2"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.room_id").value(room.getRoomId()))
                .andExpect(jsonPath("$.user_id").value(user.getUserId()));
    }

    // ---------- GET ALL ----------
    @Test
    @WithMockUser(roles = {"USER"})
    void testGetAllBookings() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(List.of(booking));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].purpose").value("Lecture"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    // ---------- GET BY ID ----------
    @Test
    @WithMockUser(roles = {"USER"})
    void testGetBookingById() throws Exception {
        when(bookingService.getBookingById(booking.getBookingId())).thenReturn(booking);

        mockMvc.perform(get("/api/bookings/{id}", booking.getBookingId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purpose").value("Lecture"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    // ---------- UPDATE ----------
    @Test
    @WithMockUser(roles = {"USER"})
    void testUpdateBooking() throws Exception {
        BookingCreateDTO updateDTO = new BookingCreateDTO();
        updateDTO.setPurpose("Updated Lecture");
        updateDTO.setStart_time(LocalDateTime.of(2025, 9, 15, 10, 0));
        updateDTO.setEnd_time(LocalDateTime.of(2025, 9, 15, 12, 0));
        updateDTO.setRoom_id(room.getRoomId());
        updateDTO.setUser_id(user.getUserId());

        BookingResponseDTO updatedResponse = new BookingResponseDTO();
        updatedResponse.setBooking_id(booking.getBookingId());
        updatedResponse.setPurpose("Updated Lecture");
        updatedResponse.setStart_time(updateDTO.getStart_time());
        updatedResponse.setEnd_time(updateDTO.getEnd_time());
        updatedResponse.setStatus(BookingStatus.PENDING);
        updatedResponse.setRoom_id(room.getRoomId());
        updatedResponse.setRoom_name(room.getName());
        updatedResponse.setUser_id(user.getUserId());
        updatedResponse.setUsername(user.getUsername());

        when(bookingService.updateBooking(eq(booking.getBookingId()), any(BookingCreateDTO.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/bookings/{id}", booking.getBookingId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purpose").value("Updated Lecture"))
                .andExpect(jsonPath("$.room_id").value(room.getRoomId()));
    }

    // ---------- CANCEL ----------
    @Test
    @WithMockUser(roles = {"USER"})
    void testCancelBooking() throws Exception {
        doNothing().when(bookingService).cancelBooking(booking.getBookingId());

        mockMvc.perform(put("/api/bookings/{id}/cancel", booking.getBookingId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking cancelled successfully."));
    }
}