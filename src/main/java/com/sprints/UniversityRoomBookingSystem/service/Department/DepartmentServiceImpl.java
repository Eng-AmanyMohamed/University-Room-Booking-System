package com.sprints.UniversityRoomBookingSystem.service.Department;

import com.sprints.UniversityRoomBookingSystem.Exception.DataNotFoundException;
import com.sprints.UniversityRoomBookingSystem.dto.request.DepartmentCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.DepartmentResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Department;
import com.sprints.UniversityRoomBookingSystem.repository.DepartmentRepository;
import com.sprints.UniversityRoomBookingSystem.service.Department.DepartmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponseDTO createDepartment(DepartmentCreateDTO dto) {
        Department department = new Department();
        department.setName(dto.getName());
        departmentRepository.save(department);

        return mapToDTO(department);
    }

    @Override
    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Department not found with id: " + id));
        return mapToDTO(department);
    }

    @Override
    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(dept -> mapToDTO(dept))
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentCreateDTO dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Department not found with id: " + id));

        department.setName(dto.getName());
        // update code & description in entity if added
        departmentRepository.save(department);

        return mapToDTO(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new DataNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }

    private DepartmentResponseDTO mapToDTO(Department department) {
        return new DepartmentResponseDTO(
                department.getDepartId(),
                department.getName()
        );
    }
}
