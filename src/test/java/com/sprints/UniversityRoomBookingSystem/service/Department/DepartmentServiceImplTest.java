package com.sprints.UniversityRoomBookingSystem.service.Department;

import com.sprints.UniversityRoomBookingSystem.dto.request.DepartmentCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.DepartmentResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Department;
import com.sprints.UniversityRoomBookingSystem.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        department = new Department();
        department.setDepartId(1L);
        department.setName("Computer Science");
    }

    @Test
    void createDepartment_ShouldSaveAndReturnDTO() {
        DepartmentCreateDTO dto = new DepartmentCreateDTO("Computer Science");
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        DepartmentResponseDTO result = departmentService.createDepartment(dto);

        assertNotNull(result);
        assertEquals("Computer Science", result.getName());

        ArgumentCaptor<Department> captor = ArgumentCaptor.forClass(Department.class);
        verify(departmentRepository).save(captor.capture());
        assertEquals("Computer Science", captor.getValue().getName());
    }

    @Test
    void getDepartmentById_ShouldReturnDTO_WhenFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        DepartmentResponseDTO result = departmentService.getDepartmentById(1L);

        assertEquals(1L, result.getDepart_id());
        assertEquals("Computer Science", result.getName());
    }

    @Test
    void getDepartmentById_ShouldThrow_WhenNotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> departmentService.getDepartmentById(1L));
    }

    @Test
    void getAllDepartments_ShouldReturnList() {
        Department dep2 = new Department();
        dep2.setDepartId(2L);
        dep2.setName("Mathematics");

        when(departmentRepository.findAll()).thenReturn(Arrays.asList(department, dep2));

        List<DepartmentResponseDTO> result = departmentService.getAllDepartments();

        assertEquals(2, result.size());
        assertEquals("Computer Science", result.get(0).getName());
        assertEquals("Mathematics", result.get(1).getName());
    }

    @Test
    void updateDepartment_ShouldUpdateAndReturnDTO_WhenFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        DepartmentCreateDTO dto = new DepartmentCreateDTO("Updated Name");
        DepartmentResponseDTO result = departmentService.updateDepartment(1L, dto);

        assertEquals("Updated Name", result.getName());
    }

    @Test
    void updateDepartment_ShouldThrow_WhenNotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> departmentService.updateDepartment(1L, new DepartmentCreateDTO("Updated")));
    }

    @Test
    void deleteDepartment_ShouldCallRepository_WhenFound() {
        when(departmentRepository.existsById(1L)).thenReturn(true);

        departmentService.deleteDepartment(1L);

        verify(departmentRepository).deleteById(1L);
    }

    @Test
    void deleteDepartment_ShouldThrow_WhenNotFound() {
        when(departmentRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> departmentService.deleteDepartment(1L));
    }
}