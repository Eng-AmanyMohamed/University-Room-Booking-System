package com.sprints.UniversityRoomBookingSystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprints.UniversityRoomBookingSystem.dto.request.RoleCreateDTO;
import com.sprints.UniversityRoomBookingSystem.model.Role;
import com.sprints.UniversityRoomBookingSystem.service.Role.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RoleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoleService roleService;

    private Role role1;
    private Role role2;

    @BeforeEach
    void setUp() {
        role1 = new Role();
        role1.setRoleId(1L);
        role1.setName("Admin");

        role2 = new Role();
        role2.setRoleId(2L);
        role2.setName("Student");
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testCreateRole() throws Exception {
        RoleCreateDTO dto = new RoleCreateDTO();
        dto.setName("Faculty");

        Role createdRole = new Role();
        createdRole.setRoleId(3L);
        createdRole.setName("Faculty");

        when(roleService.createRole(any(Role.class))).thenReturn(createdRole);

        mockMvc.perform(post("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleId").value(3L))
                .andExpect(jsonPath("$.name").value("Faculty"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testGetAllRoles() throws Exception {
        when(roleService.getAllRoles()).thenReturn(List.of(role1, role2));

        mockMvc.perform(get("/api/v1/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Admin"))
                .andExpect(jsonPath("$[1].name").value("Student"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testGetRoleById() throws Exception {
        when(roleService.getRoleById(1L)).thenReturn(role1);

        mockMvc.perform(get("/api/v1/roles/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testDeleteRole() throws Exception {
        mockMvc.perform(delete("/api/v1/roles/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
