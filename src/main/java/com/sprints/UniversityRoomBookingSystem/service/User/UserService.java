package com.sprints.UniversityRoomBookingSystem.service.User;


import com.sprints.UniversityRoomBookingSystem.dto.request.UserRegisterDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.UserResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    //UserResponseDTO registerUser(UserRegisterDTO userRegisterDTO);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    List<UserResponseDTO> getAllUsers();
    void deleteUser(Long id);
    UserResponseDTO updateUser(Long id, UserRegisterDTO userRegisterDTO);

}