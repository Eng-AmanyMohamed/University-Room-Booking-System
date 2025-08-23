package com.sprints.UniversityRoomBookingSystem.dto.response;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class AuthResponseDTO {
    private String token;
    private String username;
    private String email;
    private String roleName;
}
