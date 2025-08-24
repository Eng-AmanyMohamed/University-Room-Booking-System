package com.sprints.UniversityRoomBookingSystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprints.UniversityRoomBookingSystem.dto.request.HolidayCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.HolidayResponseDTO;
import com.sprints.UniversityRoomBookingSystem.service.Holiday.HolidayService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class HolidayControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private HolidayService holidayService; // 👈 mocked service

    private HolidayResponseDTO holiday;

    @BeforeEach
    void setUp() {
        holiday = new HolidayResponseDTO();
        holiday.setHoliday_id(1L);
        holiday.setStartDate(LocalDateTime.of(2025, 12, 24, 0, 0));
        holiday.setEndDate(LocalDateTime.of(2025, 12, 26, 0, 0));
        holiday.setDescription("Christmas Holiday");
    }

    @Test
    @WithMockUser(roles = {"Admin"})
    void testCreateHoliday() throws Exception {
        HolidayCreateDTO dto = new HolidayCreateDTO(
                LocalDateTime.of(2025, 12, 24, 0, 0),
                LocalDateTime.of(2025, 12, 26, 0, 0),
                "Christmas Holiday"
        );

        when(holidayService.createHoliday(any(HolidayCreateDTO.class))).thenReturn(holiday);

        mockMvc.perform(post("/api/holidays")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.holiday_id").value(1L))
                .andExpect(jsonPath("$.description").value("Christmas Holiday"));
    }

    @Test
    @WithMockUser(roles = {"Admin"})
    void testGetAllHolidays() throws Exception {
        when(holidayService.getAllHolidays()).thenReturn(List.of(holiday));

        mockMvc.perform(get("/api/holidays"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Christmas Holiday"));
    }

    @Test
    @WithMockUser(roles = {"Admin"})
    void testGetHolidayById() throws Exception {
        when(holidayService.getHolidayById(1L)).thenReturn(holiday);

        mockMvc.perform(get("/api/holidays/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Christmas Holiday"))
                .andExpect(jsonPath("$.holiday_id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"Admin"})
    void testUpdateHoliday() throws Exception {
        HolidayCreateDTO updateDTO = new HolidayCreateDTO(
                LocalDateTime.of(2025, 12, 24, 0, 0),
                LocalDateTime.of(2025, 12, 27, 0, 0),
                "Extended Christmas Holiday"
        );

        HolidayResponseDTO updatedHoliday = new HolidayResponseDTO(
                1L,
                updateDTO.getStartDate(),
                updateDTO.getEndDate(),
                updateDTO.getDescription()
        );

        when(holidayService.updateHoliday(eq(1L), any(HolidayCreateDTO.class))).thenReturn(updatedHoliday);

        mockMvc.perform(put("/api/holidays/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Extended Christmas Holiday"))
                .andExpect(jsonPath("$.endDate").value("2025-12-27T00:00:00"));
    }

    @Test
    @WithMockUser(roles = {"Admin"})
    void testDeleteHoliday() throws Exception {
        doNothing().when(holidayService).deleteHoliday(1L);

        mockMvc.perform(delete("/api/holidays/{id}", 1L).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Holiday deleted successfully"));
    }
}
