package com.sprints.UniversityRoomBookingSystem.controller;

import com.sprints.UniversityRoomBookingSystem.model.User;
import com.sprints.UniversityRoomBookingSystem.model.Role;
import com.sprints.UniversityRoomBookingSystem.repository.UserRepository;
import com.sprints.UniversityRoomBookingSystem.repository.RoleRepository;
import com.sprints.UniversityRoomBookingSystem.repository.DepartmentRepository;
import com.sprints.UniversityRoomBookingSystem.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        if (request.getRole() == null) {
            Optional<Role> defaultRole = roleRepository.findByName("USER");
            defaultRole.ifPresent(request::setRole);
        }

        User savedUser = userRepository.save(request);
        String token = jwtService.generateToken(savedUser);

        return ResponseEntity.ok("Registered successfully. Token: " + token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email,
                                   @RequestParam String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok("Login successful. Token: " + token);
    }
}
