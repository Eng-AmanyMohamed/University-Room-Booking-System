package com.sprints.UniversityRoomBookingSystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprints.UniversityRoomBookingSystem.dto.request.UserRegisterDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.UserResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Department;
import com.sprints.UniversityRoomBookingSystem.model.Role;
import com.sprints.UniversityRoomBookingSystem.model.User;
import com.sprints.UniversityRoomBookingSystem.repository.DepartmentRepository;
import com.sprints.UniversityRoomBookingSystem.repository.RoleRepository;
import com.sprints.UniversityRoomBookingSystem.repository.UserRepository;
import com.sprints.UniversityRoomBookingSystem.service.User.UserService;
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

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @MockitoBean
    private UserService userService; // mocked

    private User user;
    private Department department;
    private Role role;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setName("Computer Science");
        departmentRepository.save(department);

        role = new Role();
        role.setName("Admin");
        roleRepository.save(role);

        user = new User();
        user.setUsername("JohnDoe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        user.setDepartment(department);
        user.setRole(role);
        userRepository.save(user);
    }

    @Test
    @WithMockUser(roles = {"Admin"})
    void testGetAllUsers() throws Exception {
        UserResponseDTO responseDTO = new UserResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                role.getName(),
                department.getName()
        );

        when(userService.getAllUsers()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/User"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$[0].username").value("JohnDoe"))
                .andExpect(jsonPath("$[0].roleName").value("Admin"))
                .andExpect(jsonPath("$[0].departmentName").value("Computer Science"));
    }

    @Test
    @WithMockUser(roles = {"Admin"})
    void testGetUserById() throws Exception {
        when(userService.getUserById(user.getUserId())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/User/{id}", user.getUserId()))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.username").value("JohnDoe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @WithMockUser(roles = {"Admin"})
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(user.getUserId());

        mockMvc.perform(delete("/api/User/{id}", user.getUserId()).with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"Admin"})
    void testUpdateUser() throws Exception {
        UserRegisterDTO updateDTO = new UserRegisterDTO();
        updateDTO.setUsername("UpdatedJohn");
        updateDTO.setEmail("updated@example.com");
        updateDTO.setPassword("newpass123");
        updateDTO.setDepartmentId(department.getDepartId());
        updateDTO.setRoleId(role.getRoleId());

        UserResponseDTO updatedResponse = new UserResponseDTO(
                user.getUserId(),
                "UpdatedJohn",
                "updated@example.com",
                role.getName(),
                department.getName()
        );

        when(userService.updateUser(eq(user.getUserId()), any(UserRegisterDTO.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/User/{id}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("UpdatedJohn"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.roleName").value("Admin"));
    }
}
