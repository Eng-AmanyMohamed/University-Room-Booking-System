package com.sprints.UniversityRoomBookingSystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprints.UniversityRoomBookingSystem.dto.request.RoomFeatureCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.RoomFeatureResponseDTO;
import com.sprints.UniversityRoomBookingSystem.service.RoomFeature.RoomFeatureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RoomFeatureControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoomFeatureService roomFeatureService;

    private RoomFeatureResponseDTO feature;

    @BeforeEach
    void setUp() {
        feature = new RoomFeatureResponseDTO();
        feature.setFeature_id(1L);
        feature.setFeatureName("Projector");
        feature.setFeatureDescription("HD Projector for presentations");
        feature.setRoomIds(List.of(1L, 2L));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testAddFeature() throws Exception {
        RoomFeatureCreateDTO createDTO = new RoomFeatureCreateDTO(
                "Projector",
                "HD Projector for presentations"
        );

        when(roomFeatureService.createRoomFeature(any(RoomFeatureCreateDTO.class))).thenReturn(feature);

        mockMvc.perform(post("/features/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.feature_id").value(1L))
                .andExpect(jsonPath("$.featureName").value("Projector"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testGetAllFeatures() throws Exception {
        when(roomFeatureService.getRoomFeatures()).thenReturn(List.of(feature));

        mockMvc.perform(get("/features/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].featureName").value("Projector"))
                .andExpect(jsonPath("$[0].featureDescription").value("HD Projector for presentations"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testUpdateFeature() throws Exception {
        RoomFeatureCreateDTO updateDTO = new RoomFeatureCreateDTO(
                "Projector X",
                "Updated HD Projector"
        );

        RoomFeatureResponseDTO updatedFeature = new RoomFeatureResponseDTO(
                1L,
                "Projector X",
                "Updated HD Projector",
                List.of(1L, 2L)
        );

        when(roomFeatureService.updateRoom(eq(1L), any(RoomFeatureCreateDTO.class))).thenReturn(updatedFeature);

        mockMvc.perform(put("/features/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.featureName").value("Projector X"))
                .andExpect(jsonPath("$.featureDescription").value("Updated HD Projector"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testDeleteFeature() throws Exception {
        doNothing().when(roomFeatureService).deleteRoomFeature(1L);

        mockMvc.perform(delete("/features/{id}", 1L).with(csrf()))
                .andExpect(status().isNoContent());
    }
}
