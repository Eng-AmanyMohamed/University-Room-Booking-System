package com.sprints.UniversityRoomBookingSystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long user_id;
    private String username;
    private String email;
    private String roleName;
    private String departmentName;
}
