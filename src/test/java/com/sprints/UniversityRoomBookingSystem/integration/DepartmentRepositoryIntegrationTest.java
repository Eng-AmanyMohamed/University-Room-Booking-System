package com.sprints.UniversityRoomBookingSystem.integration;

import com.sprints.UniversityRoomBookingSystem.model.Department;
import com.sprints.UniversityRoomBookingSystem.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class DepartmentRepositoryIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setName("Computer Science");
        departmentRepository.save(department);
    }

    @Test
    void testFindByName_Exists() {
        Optional<Department> found = departmentRepository.findByName("Computer Science");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Computer Science");
    }

    @Test
    void testFindByName_NotExists() {
        Optional<Department> found = departmentRepository.findByName("Mathematics");
        assertThat(found).isEmpty();
    }

    @Test
    void testExistsByName_True() {
        boolean exists = departmentRepository.existsByName("Computer Science");
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByName_False() {
        boolean exists = departmentRepository.existsByName("Mathematics");
        assertThat(exists).isFalse();
    }

    @Test
    void testFindById() {
        Optional<Department> found = departmentRepository.findById(department.getDepartId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Computer Science");
    }

    @Test
    void testSaveDepartment() {
        Department newDepartment = new Department();
        newDepartment.setName("Mathematics");
        Department saved = departmentRepository.save(newDepartment);

        assertThat(saved.getDepartId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Mathematics");
    }

    @Test
    void testDeleteDepartment() {
        departmentRepository.delete(department);
        assertThat(departmentRepository.existsById(department.getDepartId())).isFalse();
    }
}
