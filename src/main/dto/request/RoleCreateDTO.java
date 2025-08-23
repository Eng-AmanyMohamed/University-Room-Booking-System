package com.sprints.UniversityRoomBookingSystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleCreateDTO {
    @NotBlank(message = "Role name is required")
    private String name;
}
