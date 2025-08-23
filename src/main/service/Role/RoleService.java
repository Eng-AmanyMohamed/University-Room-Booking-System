package com.sprints.UniversityRoomBookingSystem.service.Role;

import com.sprints.UniversityRoomBookingSystem.model.Role;

import java.util.List;

public interface RoleService {
    Role createRole(Role role);
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    void deleteRole(Long id);
}
