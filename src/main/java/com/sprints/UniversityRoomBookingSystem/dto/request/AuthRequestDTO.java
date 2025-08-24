package com.sprints.UniversityRoomBookingSystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequestDTO {
    @NotBlank
    private String username;
    @Size(min = 8)
    private String password;
}
