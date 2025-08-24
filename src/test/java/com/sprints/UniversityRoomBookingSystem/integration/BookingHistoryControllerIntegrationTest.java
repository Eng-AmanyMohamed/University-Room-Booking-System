package com.sprints.UniversityRoomBookingSystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprints.UniversityRoomBookingSystem.dto.response.BookingHistoryResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.BookingAction;
import com.sprints.UniversityRoomBookingSystem.model.BookingStatus;
import com.sprints.UniversityRoomBookingSystem.service.BookhingHistory.BookingHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookingHistoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookingHistoryService bookingHistoryService;

    private BookingHistoryResponseDTO history;

    @BeforeEach
    void setUp() {
        history = BookingHistoryResponseDTO.builder()
                .historyId(1L)
                .previousStatus(BookingStatus.PENDING)
                .newStatus(BookingStatus.APPROVED)
                .action(BookingAction.APPROVE)
                .changedAt(Instant.now())
                .reason("Approved by admin")
                .bookingId(1L)
                .changedBy("AdminUser")
                .build();
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testGetBookingHistory() throws Exception {
        when(bookingHistoryService.getHistoryForBooking(1L)).thenReturn(List.of(history));

        mockMvc.perform(get("/api/bookings/{bookingId}/history", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].historyId").value(1L))
                .andExpect(jsonPath("$[0].previousStatus").value("PENDING"))
                .andExpect(jsonPath("$[0].newStatus").value("APPROVED"))
                .andExpect(jsonPath("$[0].action").value("APPROVE"))
                .andExpect(jsonPath("$[0].reason").value("Approved by admin"))
                .andExpect(jsonPath("$[0].bookingId").value(1L))
                .andExpect(jsonPath("$[0].changedBy").value("AdminUser"));
    }
}
