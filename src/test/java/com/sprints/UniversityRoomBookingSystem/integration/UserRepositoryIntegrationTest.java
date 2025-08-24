package com.sprints.UniversityRoomBookingSystem.integration;

import com.sprints.UniversityRoomBookingSystem.model.Department;
import com.sprints.UniversityRoomBookingSystem.model.Role;
import com.sprints.UniversityRoomBookingSystem.model.User;
import com.sprints.UniversityRoomBookingSystem.repository.DepartmentRepository;
import com.sprints.UniversityRoomBookingSystem.repository.RoleRepository;
import com.sprints.UniversityRoomBookingSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User user;
    private Department department;
    private Role role;

    @BeforeEach
    void setUp() {
        // Persist Department
        department = new Department();
        department.setName("Computer Science");
        departmentRepository.save(department);

        // Persist Role
        role = new Role();
        role.setName("Admin");
        roleRepository.save(role);

        // Persist User
        user = new User();
        user.setUsername("JohnDoe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        user.setDepartment(department);
        user.setRole(role);
        userRepository.save(user);
    }

    @Test
    void testFindByEmail() {
        Optional<User> found = userRepository.findByEmail("john@example.com");
        assertThat(found).isPresent();
        //assertThat(found.get().getUsername()).isEqualTo("JohnDoe");
    }

    @Test
    void testFindByUsername() {
        Optional<User> found = userRepository.findByUsername("JohnDoe");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testExistsByEmail() {
        boolean exists = userRepository.existsByEmail("john@example.com");
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByUsername() {
        boolean exists = userRepository.existsByUsername("JohnDoe");
        assertThat(exists).isTrue();
    }

    @Test
    void testFindByEmail_NotFound() {
        Optional<User> found = userRepository.findByEmail("notfound@example.com");
        assertThat(found).isEmpty();
    }

    @Test
    void testFindByUsername_NotFound() {
        Optional<User> found = userRepository.findByUsername("UnknownUser");
        assertThat(found).isEmpty();
    }
}
