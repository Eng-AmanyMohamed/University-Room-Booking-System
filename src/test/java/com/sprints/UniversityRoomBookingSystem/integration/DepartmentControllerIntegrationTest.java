package com.sprints.UniversityRoomBookingSystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprints.UniversityRoomBookingSystem.dto.request.DepartmentCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.DepartmentResponseDTO;
import com.sprints.UniversityRoomBookingSystem.service.Department.DepartmentService;
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
public class DepartmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DepartmentService departmentService;

    private DepartmentResponseDTO department;

    @BeforeEach
    void setUp() {
        department = new DepartmentResponseDTO();
        department.setDepart_id(1L);
        department.setName("Computer Science");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testCreateDepartment() throws Exception {
        DepartmentCreateDTO createDTO = new DepartmentCreateDTO();
        createDTO.setName("Computer Science");

        when(departmentService.createDepartment(any(DepartmentCreateDTO.class))).thenReturn(department);

        mockMvc.perform(post("/api/departments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.depart_id").value(1L))
                .andExpect(jsonPath("$.name").value("Computer Science"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testGetDepartmentById() throws Exception {
        when(departmentService.getDepartmentById(1L)).thenReturn(department);

        mockMvc.perform(get("/api/departments/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.depart_id").value(1L))
                .andExpect(jsonPath("$.name").value("Computer Science"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testGetAllDepartments() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(List.of(department));

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].depart_id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Computer Science"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateDepartment() throws Exception {
        DepartmentCreateDTO updateDTO = new DepartmentCreateDTO();
        updateDTO.setName("CS Updated");

        DepartmentResponseDTO updatedDepartment = new DepartmentResponseDTO();
        updatedDepartment.setDepart_id(1L);
        updatedDepartment.setName("CS Updated");

        when(departmentService.updateDepartment(eq(1L), any(DepartmentCreateDTO.class))).thenReturn(updatedDepartment);

        mockMvc.perform(put("/api/departments/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("CS Updated"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeleteDepartment() throws Exception {
        doNothing().when(departmentService).deleteDepartment(1L);

        mockMvc.perform(delete("/api/departments/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
