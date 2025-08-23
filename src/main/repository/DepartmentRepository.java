package com.sprints.UniversityRoomBookingSystem.repository;

import com.sprints.UniversityRoomBookingSystem.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);
    Optional<Department> findById(Long id);
    boolean existsByName(String name);
}
