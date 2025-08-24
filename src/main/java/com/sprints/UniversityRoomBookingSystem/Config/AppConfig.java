package com.sprints.UniversityRoomBookingSystem.Config;

import com.sprints.UniversityRoomBookingSystem.Exception.EntityNotFoundException;
import com.sprints.UniversityRoomBookingSystem.model.User;
import com.sprints.UniversityRoomBookingSystem.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.Clock;

@Configuration

public class AppConfig {
    public class JpaConfig {
        @Bean
        public Clock clock() {
            return Clock.systemUTC();
        }
    }
    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .authorities(user.getRole().getName()) // THIS populates authorities
                    .build();
        };
    }
}
