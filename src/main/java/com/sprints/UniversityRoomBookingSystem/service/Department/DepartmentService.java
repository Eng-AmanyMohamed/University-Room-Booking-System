package com.sprints.UniversityRoomBookingSystem.service.Department;

import com.sprints.UniversityRoomBookingSystem.dto.request.DepartmentCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.DepartmentResponseDTO;

import java.util.List;

public interface DepartmentService {
    DepartmentResponseDTO createDepartment(DepartmentCreateDTO dto);
    DepartmentResponseDTO getDepartmentById(Long id);
    List<DepartmentResponseDTO> getAllDepartments();
    DepartmentResponseDTO updateDepartment(Long id, DepartmentCreateDTO dto);
    void deleteDepartment(Long id);
}