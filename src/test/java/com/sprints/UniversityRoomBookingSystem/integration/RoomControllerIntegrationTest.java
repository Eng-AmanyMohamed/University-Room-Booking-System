package com.sprints.UniversityRoomBookingSystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprints.UniversityRoomBookingSystem.dto.request.RoomCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.RoomResponseDTO;
import com.sprints.UniversityRoomBookingSystem.service.Room.RoomService;
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
public class RoomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoomService roomService;

    private RoomResponseDTO room;

    @BeforeEach
    void setUp() {
        room = new RoomResponseDTO();
        room.setRoomId(1L);
        room.setName("Room A");
        room.setCapacity(50);
        room.setBuildingName("Main Building");
        room.setFeatures(List.of("Projector", "Whiteboard"));
    }

    @Test
    @WithMockUser(roles = {"Admin"})
    void testAddRoom() throws Exception {
        RoomCreateDTO createDTO = new RoomCreateDTO(
                "Room A",
                50,
                1L,
                List.of("Projector", "Whiteboard")
        );

        when(roomService.createRoom(any(RoomCreateDTO.class))).thenReturn(room);

        mockMvc.perform(post("/room/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomId").value(1L))
                .andExpect(jsonPath("$.name").value("Room A"))
                .andExpect(jsonPath("$.capacity").value(50));
    }

    @Test
    @WithMockUser(roles = {"Admin"})
    void testGetAllRooms() throws Exception {
        when(roomService.getRooms()).thenReturn(List.of(room));

        mockMvc.perform(get("/room/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Room A"))
                .andExpect(jsonPath("$[0].buildingName").value("Main Building"));
    }

    @Test
    @WithMockUser(roles = {"Admin"})
    void testGetRoomById() throws Exception {
        when(roomService.getRoom(1L)).thenReturn(room);

        mockMvc.perform(get("/room/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Room A"))
                .andExpect(jsonPath("$.roomId").value(1L));
    }

    @Test
    @WithMockUser(roles = {"Admin"})
    void testUpdateRoom() throws Exception {
        RoomCreateDTO updateDTO = new RoomCreateDTO(
                "Room A Updated",
                60,
                1L,
                List.of("Projector", "Whiteboard", "AC")
        );

        RoomResponseDTO updatedRoom = new RoomResponseDTO(
                1L,
                "Room A Updated",
                60,
                "Main Building",
                List.of("Projector", "Whiteboard", "AC")
        );

        when(roomService.updateRoom(eq(1L), any(RoomCreateDTO.class))).thenReturn(updatedRoom);

        mockMvc.perform(put("/room/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Room A Updated"))
                .andExpect(jsonPath("$.capacity").value(60))
                .andExpect(jsonPath("$.features.length()").value(3));
    }

    @Test
    @WithMockUser(roles = {"Admin"})
    void testDeleteRoom() throws Exception {
        doNothing().when(roomService).deleteRoom(1L);

        mockMvc.perform(delete("/room/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
