package com.sprints.UniversityRoomBookingSystem.service.User;


import com.sprints.UniversityRoomBookingSystem.Exception.EntityNotFoundException;
import com.sprints.UniversityRoomBookingSystem.dto.request.AuthRequestDTO;
import com.sprints.UniversityRoomBookingSystem.dto.request.UserRegisterDTO;
import com.sprints.UniversityRoomBookingSystem.dto.request.UserUpdateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.AuthResponseDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.UserResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Department;
import com.sprints.UniversityRoomBookingSystem.model.Role;
import com.sprints.UniversityRoomBookingSystem.model.User;
import com.sprints.UniversityRoomBookingSystem.repository.DepartmentRepository;
import com.sprints.UniversityRoomBookingSystem.repository.RoleRepository;
import com.sprints.UniversityRoomBookingSystem.repository.UserRepository;
import com.sprints.UniversityRoomBookingSystem.util.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder, DepartmentRepository departmentRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public UserResponseDTO registerUser(UserRegisterDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // 🔒 hash
        Optional<Role> role = roleRepository.findById( dto.getRoleId());
        if(role.isPresent()) {
            user.setRole(role.get());
        }else{
            throw new EntityNotFoundException("Role not found");
        }
        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().getName(),
                null
        );
    }


    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserResponseDTO(
                        u.getUserId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getRole().getName(),
                        u.getDepartment().getName()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO updateUser(Long id,  UserUpdateDTO userUpdateDTO){
        User user  = userRepository.findById(id).
                orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found with id: " + id));


        user.setUsername(userUpdateDTO.getUsername());
        user.setEmail(userUpdateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));

        Optional< Department> department = departmentRepository.findById( userUpdateDTO.getDepartmentId());
        if(department.isPresent()) {
            user.setDepartment(department.get());
        }else{
            throw new EntityNotFoundException("Department not found");
        }

        Optional<Role> role = roleRepository.findById( userUpdateDTO.getRoleId());
        if(role.isPresent()) {
            user.setRole(role.get());
        }else{
            throw new EntityNotFoundException("Role not found");
        }
        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().getName(),
                savedUser.getDepartment().getName()
        );
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String token = jwtService.generateToken(username);

        return new AuthResponseDTO(token);
    }
}
