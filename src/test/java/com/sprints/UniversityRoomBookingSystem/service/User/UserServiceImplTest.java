package com.sprints.UniversityRoomBookingSystem.service.User;

import com.sprints.UniversityRoomBookingSystem.Exception.EntityNotFoundException;
import com.sprints.UniversityRoomBookingSystem.dto.request.UserRegisterDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.UserResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Department;
import com.sprints.UniversityRoomBookingSystem.model.Role;
import com.sprints.UniversityRoomBookingSystem.model.User;
import com.sprints.UniversityRoomBookingSystem.repository.DepartmentRepository;
import com.sprints.UniversityRoomBookingSystem.repository.RoleRepository;
import com.sprints.UniversityRoomBookingSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Department department;
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        department = new Department();
        department.setDepartId(1L);
        department.setName("Computer Science");

        role = new Role();
        role.setRoleId(1L);
        role.setName("Admin");

        user = new User();
        user.setUserId(1L);
        user.setUsername("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("pass");
        user.setDepartment(department);
        user.setRole(role);
    }

//    @Test
//    void getUserById_ShouldReturnUser_WhenFound() {
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        Optional<User> result = userService.getUserById(1L);
//
//        assertTrue(result.isPresent());
//        assertEquals("John Doe", result.get().getUsername());
//    }

    @Test
    void getUserById_ShouldReturnEmpty_WhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getUserByEmail_ShouldReturnUser_WhenFound() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail("john@example.com");

        assertTrue(result.isPresent());
        assertEquals("john@example.com", result.get().getEmail());
    }

    @Test
    void getUserByEmail_ShouldReturnEmpty_WhenNotFound() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByEmail("john@example.com");

        assertTrue(result.isEmpty());
    }

//    @Test
//    void getAllUsers_ShouldReturnList() {
//        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
//
//        List<UserResponseDTO> result = userService.getAllUsers();
//
//        assertEquals(1, result.size());
//        assertEquals("John Doe", result.get(0).getUsername());
//        assertEquals("Admin", result.get(0).getRoleName());
//        assertEquals("Computer Science", result.get(0).getDepartmentName());
//    }

    @Test
    void deleteUser_ShouldDelete_WhenExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_ShouldThrow_WhenNotExists() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1L));
    }

//    @Test
//    void updateUser_ShouldUpdateAndReturnDTO_WhenValid() {
//        UserRegisterDTO dto = new UserRegisterDTO(
//                "Updated Name", "updated@example.com", "newpass", 1L, 1L
//        );
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(passwordEncoder.encode("newpass")).thenReturn("encodedPass");
//        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
//        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
//        when(userRepository.save(any(User.class))).thenReturn(user);
//
//        UserResponseDTO result = userService.updateUser(1L, dto);
//
//        assertEquals("Updated Name", result.getUsername());
//        assertEquals("updated@example.com", result.getEmail());
//    }

    @Test
    void updateUser_ShouldThrow_WhenDepartmentNotFound() {
        UserRegisterDTO dto = new UserRegisterDTO(
                "Updated", "updated@example.com", "pass", 99L, 1L
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(1L, dto));
    }

    @Test
    void updateUser_ShouldThrow_WhenRoleNotFound() {
        UserRegisterDTO dto = new UserRegisterDTO(
                "Updated", "updated@example.com", "pass", 1L, 99L
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(1L, dto));
    }
}